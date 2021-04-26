package com.kwarchart.android.chart

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwarchart.android.Chart
import com.kwarchart.android.enum.BarChartType
import com.kwarchart.android.enum.BarChartType.*
import com.kwarchart.android.enum.BarStyleType
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.model.BarSeries
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.model.Legend

private const val AXIS_VALUES_FONT_SIZE = 32f

private var mHGap = 0f
private var mVGap = 0f
private var mMaxVal = 0f
private var mMaxLen = 0

/**
 *
 */
@Composable
fun <T> BarChart(
        modifier: Modifier = Modifier,
        data: List<BarSeries<T>>,
        xAxisName: String? = null,
        yAxisName: String? = null,
        axesColor: Color = Color.Gray,
        showAxes: Boolean = true,
        gridsColor: Color = Color.Gray,
        showGrid: Boolean = true,
        legendPos: LegendPosition? = null,
        type: BarChartType = VERTICAL
) {
    val keys = mutableListOf<T>()

    data.forEach {
        if (mMaxLen < it.data.size) {
            mMaxLen = it.data.size
        }
        it.data.forEach { chartData ->
            if (mMaxVal < chartData.value) {
                mMaxVal = chartData.value
            }
            if (!keys.contains(chartData.key)) {
                keys.add(chartData.key)
            }
        }
    }
    Chart(
            modifier = modifier,
            xAxisName = xAxisName,
            yAxisName = yAxisName,
            legend = legendPos?.let { _ ->
                val tmpLegend = Legend(legendPos)
                data.forEach {
                    tmpLegend.legends.add(it.legend)
                    tmpLegend.colors.add(it.color)
                }
                tmpLegend
            }
    ) {
        Canvas(
                modifier = modifier
                        .weight(5f)
                        .padding(
                                start = (mMaxVal.toString().length * 8).dp,
                                top = 10.dp,
                                end = 10.dp,
                                bottom = AXIS_VALUES_FONT_SIZE.dp
                        )
        ) {
            mHGap = size.width / (mMaxLen + 0.5f)
            mVGap = size.height / mMaxLen
            if (showGrid) {
                drawGrids(mMaxLen, gridsColor)
            }
            if (showAxes) {
                drawAxes(axesColor, keys)
            }
            data.forEachIndexed { index, element ->
                drawData(element, index, data.size, type)
            }
        }
    }
}

/**
 * Plot line chart data.
 *
 * @param barSeries Data to be plotted in this chart.
 */
private fun <T> DrawScope.drawData(barSeries: BarSeries<T>, pos: Int, totalSize: Int, barChartType: BarChartType) {
    barSeries.data.forEachIndexed { i, chartData ->
        val point = getDataPoint(i, chartData, size.height)
        when (barChartType) {
            VERTICAL -> {
                if (totalSize < 2)
                    // Draw a single bar
                    drawRect(
                            topLeft = Offset(point.x - barSeries.width, point.y),
                            size = Size(width = barSeries.width, height = size.height - point.y),
                            color = barSeries.color,
                            style = getBarStyle(barSeries.style),
                    )
                else {
                    // Draw 2 bars
                    if (pos == 0)
                        drawRect(
                                topLeft = Offset(point.x - 21, point.y),
                                size = Size(width = barSeries.width, height = size.height - point.y),
                                color = barSeries.color,
                                style = getBarStyle(barSeries.style),
                        )
                    else
                        drawRect(
                                topLeft = Offset(point.x + 1, point.y),
                                size = Size(width = barSeries.width, height = size.height - point.y),
                                color = barSeries.color,
                                style = getBarStyle(barSeries.style),
                        )
                }
            }
            VERTICAL_STACKED -> drawRect(
                    topLeft = Offset(point.x - barSeries.width, point.y),
                    size = Size(width = barSeries.width, height = size.height - point.y),
                    color = barSeries.color,
                    style = getBarStyle(barSeries.style),
            )
            HORIZONTAL -> TODO()
            HORIZONTAL_STACKED -> TODO()
        }
    }
}

/**
 * Get position of data point to be plotted in chart.
 *
 * @param index Data index.
 * @param data Chart data.
 * @param canvasHeight Canvas' height.
 *
 * @return Data point's position in canvas.
 */
private fun <T> getDataPoint(
        index: Int,
        data: ChartData<T>,
        canvasHeight: Float
) = Offset(
        (index + 1) * mHGap,
        canvasHeight - ((data.value / mMaxVal) * canvasHeight)
)

/**
 * Canvas' origin point.
 *
 * @return Offset point.
 */
private fun DrawScope.origin() = Offset(0f, size.height)

/**
 * Draw line chart grids.
 *
 * @param count Grid count.
 * @param color Grid color.
 */
private fun DrawScope.drawGrids(count: Int, color: Color) {
    val startPoint = origin()

    for (i in 1..count) {
        // Horizontal lines
        drawLine(
                color = color,
                start = Offset(0f, startPoint.y - i * mVGap),
                end = Offset(size.width, startPoint.y - i * mVGap),
        )
        // Vertical lines
//        drawLine(
//                color = color,
//                start = Offset(i * mHGap, 0f),
//                end = Offset(i * mHGap, size.height),
//        )
    }
}

/**
 * Draw the bars with its preferred style.
 *
 * @param style BarStyleType.
 */
private fun getBarStyle(style: BarStyleType): DrawStyle {
    if (style == BarStyleType.FILL) return Fill
    return Stroke(3f)
}

/**
 * Draw X and Y axes.
 *
 * @param color Axes color.
 * @param keys Keys to be plotted in the X-axis.
 */
private fun <T> DrawScope.drawAxes(
        color: Color,
        keys: List<T>
) {
    val startPoint = origin()

    // X-axis
    drawLine(
            color = color,
            start = startPoint,
            end = Offset(size.width, startPoint.y)
    )
    // Y-axis
    drawLine(
            color = color,
            start = startPoint,
            end = Offset(0f, 0f)
    )
    drawIntoCanvas {
        val valuePerGrid = mMaxVal / mMaxLen
        val valTextPaint = Paint()
        valTextPaint.textAlign = Paint.Align.RIGHT
        valTextPaint.textSize = AXIS_VALUES_FONT_SIZE
        valTextPaint.color = 0xff000000.toInt()
        val keyTextPaint = Paint()
        keyTextPaint.textAlign = Paint.Align.CENTER
        keyTextPaint.textSize = AXIS_VALUES_FONT_SIZE
        keyTextPaint.color = 0xff000000.toInt()

        keys.forEachIndexed { i, key ->
            val valOffset = Offset(
                    -20f,
                    (startPoint.y - (i + 1) * mVGap) + AXIS_VALUES_FONT_SIZE / 2
            )
            val keyOffset = Offset(
                    (i + 1) * mHGap,
                    startPoint.y + AXIS_VALUES_FONT_SIZE + 10f
            )

            it.nativeCanvas.drawText(
                    (valuePerGrid * (i + 1)).toInt().toString(),
                    valOffset.x,
                    valOffset.y,
                    valTextPaint
            )
            it.nativeCanvas.drawText(
                    key.toString(),
                    keyOffset.x,
                    keyOffset.y,
                    keyTextPaint
            )
        }
    }
}

@Preview
@Composable
fun BarChartPreview() {
    BarChart(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(color = Color.White),
            xAxisName = "X Axis",
            yAxisName = "Y Axis",
            data = arrayListOf(
                    BarSeries(
                            data = mutableListOf(
                                    ChartData("january", 100f),
                                    ChartData(2, 300f),
                                    ChartData(3, 1100f),
                                    ChartData(4, 200f),
                                    ChartData(5, 850f),
                                    ChartData(6, 400f),
                                    ChartData(7, 610f)
                            ),
                            color = Color.Green,
                            legend = "Budget"
                    ),
                    BarSeries(
                            data = mutableListOf(
                                    ChartData("january", 50f),
                                    ChartData(2, 350f),
                                    ChartData(3, 250f),
                                    ChartData(4, 200f),
                                    ChartData(5, 800f),
                                    ChartData(6, 500f),
                                    ChartData(7, 600f)
                            ),
                            legend = "Spent"
                    )
            ),
/// Change type to change Compose Preview
            type = VERTICAL,
//            type = VERTICAL_STACKED,
//            type = HORIZONTAL,
//            type = HORIZONTAL_STACKED,
    )
}