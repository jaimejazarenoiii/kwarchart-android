package com.kwarchart.android.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwarchart.android.Chart
import com.kwarchart.android.ChartCanvas
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.enum.LineChartType
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.model.Legend
import com.kwarchart.android.model.LineSeries
import com.kwarchart.android.util.PathUtils

private var mHGap = 0f
private var mVGap = 0f
private var mMaxVal = 0f

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
    xAxisName: String? = null,
    yAxisName: String? = null,
    axesColor: Color = Color.Gray,
    showAxes: Boolean = true,
    gridsColor: Color = Color.Gray,
    showGrid: Boolean = true,
    legendPos: LegendPosition? = null
) {
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
        ChartCanvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(5f)
                .padding(10.dp)
        ) {
            var maxSize = 0

            data.forEach {
                if (maxSize < it.data.size) {
                    maxSize = it.data.size
                }

                mHGap = size.width / maxSize
                mVGap = size.height / maxSize

                it.data.forEach { chartData ->
                    if (mMaxVal < chartData.value) {
                        mMaxVal = chartData.value
                    }
                }
            }


            if (showGrid) {
                drawGrids(this, maxSize, gridsColor)
            }
            if (showAxes) {
                drawAxes(this, axesColor)
            }
            data.forEach {
                drawData(this, it)
            }
        }
    }
}

/**
 * Draw line chart grids.
 *
 * @param drawScope Canvas' DrawScope.
 * @param count Grid count.
 * @param color Grid color.
 */
private fun drawGrids(
    drawScope: DrawScope,
    count: Int,
    color: Color
) {
//    val pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(10f, 10f))

    for (i in 1..count) {
        // Horizontal lines
        drawScope.drawLine(
            color = color,
            start = Offset(0f, i * mVGap),
            end = Offset(drawScope.size.width, i * mVGap),
//            pathEffect = pathEffect
        )
        // Vertical lines
        drawScope.drawLine(
            color = color,
            start = Offset(i * mHGap, 0f),
            end = Offset(i * mHGap, drawScope.size.height),
//            pathEffect = pathEffect
        )
    }
}

/**
 * Draw X and Y axes.
 *
 * @param drawScope Canvas' DrawScope.
 * @param color Axes color.
 */
private fun drawAxes(drawScope: DrawScope, color: Color) {
    // X-axis
    drawScope.drawLine(
        color = color,
        start = Offset(0f, 0f),
        end = Offset(drawScope.size.width, 0f)
    )
    // Y-axis
    drawScope.drawLine(
        color = color,
        start = Offset(0f, 0f),
        end = Offset(0f, drawScope.size.height)
    )
}

/**
 * Plot line chart data.
 *
 * @param drawScope Canvas' DrawScope.
 * @param lineSeries Data to be plotted in this chart.
 */
private fun <T> drawData(
    drawScope: DrawScope,
    lineSeries: LineSeries<T>
) {
    val path = Path()
    var leftPoint = Offset(0f, 0f)

    lineSeries.data.forEachIndexed { i, chartData ->
        val p = getDataPoint(i, chartData, drawScope.size.height)

        if (lineSeries.type == LineChartType.NORMAL) {
            path.lineTo(p.x, p.y)
            return@forEachIndexed
        }

        val rightPointIndex = if (i + 1 < lineSeries.data.size) i + 1 else i
        val rightPoint = getDataPoint(
            i + 1,
            lineSeries.data[rightPointIndex],
            drawScope.size.height
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

    drawScope.drawPath(
        path = path,
        color = lineSeries.color,
        style = Stroke(lineSeries.width.toFloat())
    )

    if (lineSeries.showDataPoint) {
        lineSeries.data.forEachIndexed { i, chartData ->
            drawScope.drawCircle(
                color = lineSeries.dataPointColor,
                radius = 10f,
                center = getDataPoint(i, chartData, drawScope.size.height)
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
    (data.value / mMaxVal) * canvasHeight
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