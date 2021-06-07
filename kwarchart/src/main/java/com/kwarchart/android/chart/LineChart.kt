package com.kwarchart.android.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwarchart.android.*
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.enum.LineChartType
import com.kwarchart.android.model.*
import com.kwarchart.android.util.ChartUtils
import com.kwarchart.android.util.PathUtils

private const val WEIGHT_LINE_CHART_CANVAS = 5f

/**
 * Line chart.
 *
 * @param modifier Modifier.
 * @param data List of LineSeries which will be plotted in this chart.
 * @param title Chart title.
 * @param axesStyle X and Y axes style.
 * @param gridsStyle Grids style.
 * @param legendPos Legend position.
 */
@Composable
fun <T> LineChart(
    modifier: Modifier = Modifier,
    data: List<LineSeries<T>>,
    title: String? = null,
    axesStyle: AxesStyle = AxesStyle(),
    gridsStyle: GridsStyle = GridsStyle(),
    legendPos: LegendPosition? = null
) {
    val keys = mutableListOf<T>()

    var maxLen = 0
    var maxVal = 0f

    data.forEach {
        if (maxLen < it.data.size) {
            maxLen = it.data.size
        }

        it.data.forEach { chartData ->
            if (maxVal < chartData.value) {
                maxVal = chartData.value
            }

            if (!keys.contains(chartData.key)) {
                keys.add(chartData.key)
            }
        }
    }

    Chart(
        modifier = modifier,
        title = title,
        axesStyle = axesStyle,
        legends = data.map {
            it.legend
        },
        legendColors = data.map {
            it.colors.first()
        },
        legendPos = legendPos
    ) {
        Canvas(
            modifier = modifier
                .weight(WEIGHT_LINE_CHART_CANVAS)
                .padding(
                    start = (maxVal.toString().length * 8).dp,
                    top = 10.dp,
                    end = 10.dp,
                    bottom = axesStyle.xValueFontStyle.size.dp
                )
        ) {
            drawGrids(gridsStyle, maxLen)

            data.forEach {
                drawData(it, maxLen, maxVal)
            }

            drawAxes(axesStyle, keys, maxVal, maxLen)
        }
    }
}

/**
 * Plot line chart data.
 *
 * @param lineSeries Data to be plotted in this chart.
 * @param maxLen Max length of all passed data in LineChart.
 * @param maxVal Max value of all passed data in LineChart.
 */
private fun <T> DrawScope.drawData(
    lineSeries: LineSeries<T>,
    maxLen: Int,
    maxVal: Float
) {
    val path = Path()
    val areaPath = if (lineSeries.type == LineChartType.AREA) {
        Path()
    } else {
        null
    }
    val vGap = size.width / maxLen

    var leftPoint = origin()
    var lastX = 0f

    path.moveTo(leftPoint.x, leftPoint.y)
    areaPath?.moveTo(leftPoint.x, leftPoint.y)

    lineSeries.data.forEachIndexed { i, chartData ->
        val p = ChartUtils.getDataPoint(i, chartData, size.height, vGap, maxVal)

        if (lineSeries.type != LineChartType.SMOOTH) {
            path.lineTo(p.x, p.y)
            areaPath?.lineTo(p.x, p.y)
            lastX = p.x
            return@forEachIndexed
        }

        val rightPointIndex = if (i + 1 < lineSeries.data.size) i + 1 else i
        val rightPoint = ChartUtils.getDataPoint(
            i + 1,
            lineSeries.data[rightPointIndex],
            size.height,
            vGap,
            maxVal
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

    val gradientColor = if (lineSeries.colors.size == 1)
        arrayListOf(
            lineSeries.colors.first(),
            lineSeries.colors.first()
        )
    else lineSeries.colors

    if (lineSeries.type == LineChartType.AREA) {
        areaPath?.lineTo(lastX, leftPoint.y)
        drawPath(
            path = areaPath!!,
            brush = Brush.horizontalGradient(gradientColor.map {
                Color(
                    red = it.red,
                    green = it.green,
                    blue = it.blue,
                    alpha = 0.5f
                )
            }),
        )
    }

    drawPath(
        path = path,
        brush = Brush.horizontalGradient(gradientColor),
        style = Stroke(lineSeries.width.toFloat())
    )

    if (lineSeries.showDataPoint) {
        lineSeries.data.forEachIndexed { i, chartData ->
            drawCircle(
                color = lineSeries.dataPointColor,
                radius = 10f,
                center = ChartUtils.getDataPoint(i, chartData, size.height, vGap, maxVal)
            )
        }
    }
}

@Preview
@Composable
fun LineChartNormalPreview() {
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = Color.White),
        axesStyle = AxesStyle(
            xName = "X Axis",
            yName = "Y Axis",
        ),
        data = arrayListOf(
            LineSeries(
                data = arrayListOf(
                    ChartData(1, 50f),
                    ChartData(2, 350f),
                    ChartData(3, 250f),
                    ChartData(4, 200f),
                    ChartData(5, 800f),
                ),
                legend = Legend("Data")
            )
        )
    )
}

@Preview
@Composable
fun LineChartSmoothPreview() {
  LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = Color.White),
      axesStyle = AxesStyle(
          xName = "X Axis",
          yName = "Y Axis",
      ),
        data = arrayListOf(
            LineSeries(
                data = arrayListOf(
                    ChartData(1, 50f),
                    ChartData(2, 350f),
                    ChartData(3, 250f),
                    ChartData(4, 200f),
                    ChartData(5, 800f),
                ),
                type = LineChartType.SMOOTH,
                legend = Legend("Data")
            )
        )
    )
}

@Preview
@Composable
fun LineChartAreaPreview() {
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = Color.White),
        axesStyle = AxesStyle(
            xName = "X Axis",
            yName = "Y Axis",
        ),
        data = arrayListOf(
            LineSeries(
                data = arrayListOf(
                    ChartData(1, 50f),
                    ChartData(2, 350f),
                    ChartData(3, 250f),
                    ChartData(4, 200f),
                    ChartData(5, 800f),
                ),
                type = LineChartType.AREA,
                legend = Legend("Data")
            )
        )
    )
}