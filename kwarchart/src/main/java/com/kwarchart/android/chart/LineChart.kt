package com.kwarchart.android.chart

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwarchart.android.Chart
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.enum.LineChartType
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.model.Legend
import com.kwarchart.android.model.LineSeries
import com.kwarchart.android.util.ChartUtils
import com.kwarchart.android.util.PathUtils

private const val WEIGHT_LINE_CHART_CANVAS = 5f
private const val AXIS_VALUES_FONT_SIZE = 32f

private var mHGap = 0f
private var mVGap = 0f
private var mMaxVal = 0f
private var mMaxLen = 0

/**
 * Line chart.
 *
 * @param modifier Modifier.
 * @param data List of LineSeries which will be plotted in this chart.
 * @param xAxisName X-axis name.
 * @param yAxisName Y-axis name.
 * @param axesColor X and Y axes color
 * @param showAxes Displayed state of axes.
 * @param gridsColor Grids color.
 * @param showGrid Displayed state of grids.
 * @param legendPos Legend position.
 */
@Composable
fun <T> LineChart(
    modifier: Modifier = Modifier,
    data: List<LineSeries<T>>,
    title: String? = null,
    xAxisName: String? = null,
    yAxisName: String? = null,
    axesColor: Color = Color.Gray,
    showAxes: Boolean = true,
    gridsColor: Color = Color.Gray,
    showGrid: Boolean = true,
    legendPos: LegendPosition? = null
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
        title = title,
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
                .weight(WEIGHT_LINE_CHART_CANVAS)
                .padding(
                    start = (mMaxVal.toString().length * 8).dp,
                    top = 10.dp,
                    end = 10.dp,
                    bottom = AXIS_VALUES_FONT_SIZE.dp
                )
        ) {
            mHGap = size.width / mMaxLen
            mVGap = size.height / mMaxLen

            if (showGrid) {
                drawGrids(mMaxLen, gridsColor)
            }
            if (showAxes) {
                drawAxes(axesColor, keys)
            }
            data.forEach {
                drawData(it)
            }
        }
    }
}

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
//    val pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(10f, 10f))

    for (i in 1..count) {
        // Horizontal lines
        drawLine(
            color = color,
            start = Offset(0f, startPoint.y - i * mVGap),
            end = Offset(size.width, startPoint.y - i * mVGap),
//            pathEffect = pathEffect
        )
        // Vertical lines
        drawLine(
            color = color,
            start = Offset(i * mHGap, 0f),
            end = Offset(i * mHGap, size.height),
//            pathEffect = pathEffect
        )
    }
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
        val valTextPaint = Paint()
        valTextPaint.textAlign = Paint.Align.RIGHT
        valTextPaint.textSize = AXIS_VALUES_FONT_SIZE
        valTextPaint.color = 0xff000000.toInt()

        val keyTextPaint = Paint()
        keyTextPaint.textAlign = Paint.Align.CENTER
        keyTextPaint.textSize = AXIS_VALUES_FONT_SIZE
        keyTextPaint.color = 0xff000000.toInt()

        ChartUtils.getAxisValues(mMaxVal, mMaxLen).forEachIndexed { i, value ->
            val valOffset = Offset(
                -20f,
                (startPoint.y - (i + 1) * mVGap) + AXIS_VALUES_FONT_SIZE / 2
            )
            val keyOffset = Offset(
                (i + 1) * mHGap,
                startPoint.y + AXIS_VALUES_FONT_SIZE + 10f
            )

            it.nativeCanvas.drawText(
                value.toInt().toString(),
                valOffset.x,
                valOffset.y,
                valTextPaint
            )
            it.nativeCanvas.drawText(
                keys[i].toString(),
                keyOffset.x,
                keyOffset.y,
                keyTextPaint
            )
        }
    }
}

/**
 * Plot line chart data.
 *
 * @param lineSeries Data to be plotted in this chart.
 */
private fun <T> DrawScope.drawData(lineSeries: LineSeries<T>) {
    val path = Path()
    var leftPoint = origin()

    path.moveTo(leftPoint.x, leftPoint.y)

    lineSeries.data.forEachIndexed { i, chartData ->
        val p = getDataPoint(i, chartData, size.height)

        if (lineSeries.type == LineChartType.NORMAL) {
            path.lineTo(p.x, p.y)
            return@forEachIndexed
        }

        val rightPointIndex = if (i + 1 < lineSeries.data.size) i + 1 else i
        val rightPoint = getDataPoint(
            i + 1,
            lineSeries.data[rightPointIndex],
            size.height
        )
        val controlPoints = PathUtils.calculateControlPoints(
            leftPoint, p, rightPoint, i == 0
        )
        path.cubicTo(
            controlPoints.x1, controlPoints.y1,
            controlPoints.x2, controlPoints.y2,
            p.x, p.y
        )

        leftPoint = p
    }

    drawPath(
        path = path,
        color = lineSeries.color,
        style = Stroke(lineSeries.width.toFloat())
    )

    if (lineSeries.showDataPoint) {
        lineSeries.data.forEachIndexed { i, chartData ->
            drawCircle(
                color = lineSeries.dataPointColor,
                radius = 10f,
                center = getDataPoint(i, chartData, size.height)
            )
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


@Preview
@Composable
fun LineChartPreview() {
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = Color.White),
        xAxisName = "X Axis",
        yAxisName = "Y Axis",
        data = arrayListOf(
            LineSeries(
                data = arrayListOf(
                    ChartData(1, 50f),
                    ChartData(2, 350f),
                    ChartData(3, 250f),
                    ChartData(4, 200f),
                    ChartData(5, 800f),
                ),
                legend = "Data"
            )
        )
    )
}