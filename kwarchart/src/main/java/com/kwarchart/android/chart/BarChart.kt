package com.kwarchart.android.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwarchart.android.*
import com.kwarchart.android.enum.BarChartType
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.model.*
import com.kwarchart.android.util.ChartUtils

/**
 * Bar chart.
 *
 * @param modifier Modifier.
 * @param data List of BarSeries which will be plotted in this chart.
 * @param type Chart type for Bar.
 * @param title Chart title.
 * @param axesStyle X and Y axes style.
 * @param gridsColor Grids color.
 * @param showGrid Displayed state of grids.
 * @param legendPos Legend position.
 */
@Composable
fun <T> BarChart(
    modifier: Modifier = Modifier,
    data: List<BarSeries<T>>,
    type: BarChartType = BarChartType.VERTICAL,
    title: String? = null,
    axesStyle: AxesStyle = AxesStyle(),
    gridsStyle: GridsStyle = GridsStyle(
        horizontal = Style(
            show = type == BarChartType.VERTICAL || type == BarChartType.VERTICAL_STACKED
        ),
        vertical = Style(
            show = type == BarChartType.HORIZONTAL || type == BarChartType.HORIZONTAL_STACKED
        )
    ),
    legendPos: LegendPosition? = null
) {
    val keys = mutableListOf<T>()
    val maxVals = arrayListOf<Float>()
    var maxLen = 0

    data.forEachIndexed { i, barSeries ->
        maxVals.add(0f)

        if (maxLen < barSeries.data.size) {
            maxLen = barSeries.data.size
        }
        barSeries.data.forEach { chartData ->
            if (maxVals[i] < chartData.value) {
                maxVals[i] = chartData.value
            }
            if (!keys.contains(chartData.key)) {
                keys.add(chartData.key)
            }
        }
    }

    val maxVal = when (type) {
        BarChartType.VERTICAL, BarChartType.HORIZONTAL -> maxVals.maxOrNull()!!

        BarChartType.VERTICAL_STACKED, BarChartType.HORIZONTAL_STACKED -> maxVals.sum()
    }

    Chart(
        modifier = modifier,
        title = title,
        axesStyle = axesStyle,
        legends = data.map {
            it.legend
        },
        legendColors = data.map {
            it.color
        },
        legendPos = legendPos
    ) {
        Canvas(
            modifier = modifier
                .weight(5f)
                .padding(
                    start = (maxVal.toString().length * 8).dp,
                    top = 10.dp,
                    end = 10.dp,
                    bottom = axesStyle.xValueFontStyle.size.dp
                )
        ) {
            val axisEndPadding = ((data.size / 2) * data.first().width) + data.first().width
            val xAxisEndPadding = if (type == BarChartType.VERTICAL || type == BarChartType.VERTICAL_STACKED)
                axisEndPadding
            else 0f

            drawGrids(
                gridsStyle,
                maxLen,
                xAxisEndPadding = xAxisEndPadding
            )

            drawData(data, type, maxLen, maxVal, xAxisEndPadding)

            drawAxes(
                axesStyle,
                keys,
                maxVal,
                maxLen,
                xAxisEndPadding = xAxisEndPadding,
                reverseKeyVal = type == BarChartType.HORIZONTAL || type == BarChartType.HORIZONTAL_STACKED
            )
        }
    }
}

/**
 * Plot line chart data.
 *
 * @param data Data to be plotted in this chart.
 * @param barChartType Type of bar to draw.
 * @param maxLen Max length of all passed data in BarChart.
 * @param maxVal Max value of all passed data in BarChart.
 * @param axisEndPadding Axis end padding.
 */
private fun <T> DrawScope.drawData(
    data: List<BarSeries<T>>,
    barChartType: BarChartType,
    maxLen: Int,
    maxVal: Float,
    axisEndPadding: Float
) {
    when (barChartType) {
        BarChartType.VERTICAL -> drawVerticalBars(data, maxLen, maxVal, axisEndPadding)

        BarChartType.VERTICAL_STACKED -> drawVerticalStackedBars(
            data, maxLen, maxVal, axisEndPadding
        )

        BarChartType.HORIZONTAL -> drawHorizontalBars(data, maxLen, maxVal, axisEndPadding)

        BarChartType.HORIZONTAL_STACKED -> drawHorizontalStackedBars(
            data, maxLen, maxVal, axisEndPadding
        )
    }
}

/**
 * Plot vertical bars data.
 *
 * @param data Data to be plotted in this chart.
 * @param maxLen Max length of all passed data in BarChart.
 * @param maxVal Max value of all passed data in BarChart.
 * @param xAxisEndPadding X-axis end padding.
 */
private fun <T> DrawScope.drawVerticalBars(
    data: List<BarSeries<T>>,
    maxLen: Int,
    maxVal: Float,
    xAxisEndPadding: Float
) {
    val path = Path()
    val halfDataSize = data.size / 2

    data.forEachIndexed { i, barSeries ->
        val vGap = (size.width - xAxisEndPadding) / maxLen
        val halfBarWidth = barSeries.width / 2
        val finalRadius = minOf(barSeries.radius, halfBarWidth)

        var deduct: Float

        if (data.size % 2 == 0) {
            deduct = (i - halfDataSize) * barSeries.width
        } else {
            if (i == halfDataSize) {
                deduct = -halfBarWidth
            } else {
                deduct = (i - halfDataSize) * barSeries.width
                deduct -= halfBarWidth
            }
        }

        barSeries.data.forEachIndexed { j, chartData ->
            val point = ChartUtils.getDataPoint(j, chartData, size.height, vGap, maxVal)
            val topLeftX = point.x + deduct
            val topRightX = topLeftX + barSeries.width

            path.reset()
            path.moveTo(topLeftX, point.y + finalRadius)
            path.quadraticBezierTo(
                topLeftX, point.y,
                topLeftX + finalRadius, point.y
            )
            path.lineTo(topRightX - finalRadius, point.y)
            path.quadraticBezierTo(
                topRightX, point.y,
                topRightX, point.y + finalRadius
            )
            path.lineTo(topRightX, size.height)
            path.lineTo(topLeftX, size.height)
            drawPath(
                path = path,
                color = barSeries.color
            )
        }
    }
}

/**
 * Plot vertical stacked bars data.
 *
 * @param data Data to be plotted in this chart.
 * @param maxLen Max length of all passed data in BarChart.
 * @param maxVal Max value of all passed data in BarChart.
 * @param xAxisEndPadding X-axis end padding.
 */
private fun <T> DrawScope.drawVerticalStackedBars(
    data: List<BarSeries<T>>,
    maxLen: Int,
    maxVal: Float,
    xAxisEndPadding: Float
) {
    val path = Path()
    val prevHeights = arrayListOf<Float>()
    var prevBottomRadius = 0f

    data.forEachIndexed { i, barSeries ->
        val vGap = (size.width - xAxisEndPadding) / maxLen
        val deduct = -barSeries.width / 2
        val finalRadius = minOf(barSeries.radius, -deduct)

        barSeries.data.forEachIndexed { j, chartData ->
            val point = ChartUtils.getDataPoint(j, chartData, size.height, vGap, maxVal)
            val topLeftX = point.x + deduct
            val topRightX = topLeftX + barSeries.width
            val barSize = Size(barSeries.width, size.height - point.y)

            var yOffset = 0f

            if (i > 0) {
                yOffset = prevHeights[j]
                prevHeights[j] = barSize.height
            } else {
                prevHeights.add(barSize.height)
            }

            path.reset()
            path.moveTo(topLeftX, point.y + finalRadius - yOffset)
            path.quadraticBezierTo(
                topLeftX, point.y - yOffset,
                topLeftX + finalRadius, point.y - yOffset
            )
            path.lineTo(topRightX - finalRadius, point.y - yOffset)
            path.quadraticBezierTo(
                topRightX, point.y - yOffset,
                topRightX, point.y + finalRadius - yOffset
            )
            path.lineTo(topRightX, size.height - yOffset + prevBottomRadius)
            path.quadraticBezierTo(
                topRightX, size.height - yOffset,
                topRightX - prevBottomRadius, size.height - yOffset
            )
            path.lineTo(topLeftX + prevBottomRadius, size.height - yOffset)
            path.quadraticBezierTo(
                topLeftX, size.height - yOffset,
                topLeftX, size.height - yOffset + prevBottomRadius
            )
            drawPath(
                path = path,
                color = barSeries.color
            )
        }

        prevBottomRadius = finalRadius
    }
}

/**
 * Plot horizontal bars data.
 *
 * @param data Data to be plotted in this chart.
 * @param maxLen Max length of all passed data in BarChart.
 * @param maxVal Max value of all passed data in BarChart.
 * @param yAxisEndPadding Y-axis end padding.
 */
private fun <T> DrawScope.drawHorizontalBars(
    data: List<BarSeries<T>>,
    maxLen: Int,
    maxVal: Float,
    yAxisEndPadding: Float
) {
    val path = Path()
    val startPoint = origin()
    val halfDataSize = data.size / 2

    data.forEachIndexed { i, barSeries ->
        val hGap = (size.height - yAxisEndPadding) / maxLen
        val halfBarWidth = barSeries.width / 2
        val finalRadius = minOf(barSeries.radius, halfBarWidth)

        var deduct: Float

        if (data.size % 2 == 0) {
            deduct = (i - halfDataSize) * barSeries.width
        } else {
            if (i == halfDataSize) {
                deduct = -halfBarWidth
            } else {
                deduct = (i - halfDataSize) * barSeries.width
                deduct -= halfBarWidth
            }
        }

        barSeries.data.forEachIndexed { j, chartData ->
            val point = ChartUtils.getDataPoint2(j, chartData, size.width, hGap, maxVal)
            val rightX = size.width - point.x
            val topY = startPoint.y - point.y + deduct
            val bottomY = topY + barSeries.width

            path.reset()
            path.moveTo(rightX - finalRadius, topY)
            path.quadraticBezierTo(
                rightX, topY,
                rightX, topY + finalRadius
            )
            path.lineTo(rightX, bottomY - finalRadius)
            path.quadraticBezierTo(
                rightX, bottomY,
                rightX - finalRadius, bottomY
            )
            path.lineTo(0f, bottomY)
            path.lineTo(0f, topY)
            drawPath(
                path = path,
                color = barSeries.color
            )
        }
    }
}

/**
 * Plot horizontal stacked bars data.
 *
 * @param data Data to be plotted in this chart.
 * @param maxLen Max length of all passed data in BarChart.
 * @param maxVal Max value of all passed data in BarChart.
 * @param yAxisEndPadding Y-axis end padding.
 */
private fun <T> DrawScope.drawHorizontalStackedBars(
    data: List<BarSeries<T>>,
    maxLen: Int,
    maxVal: Float,
    yAxisEndPadding: Float
) {
    val path = Path()
    val startPoint = origin()
    val prevWidths = arrayListOf<Float>()
    var prevBottomRadius = 0f

    data.forEachIndexed { i, barSeries ->
        val hGap = (size.height - yAxisEndPadding) / maxLen
        val deduct = -barSeries.width / 2
        val finalRadius = minOf(barSeries.radius, -deduct)

        barSeries.data.forEachIndexed { j, chartData ->
            val point = ChartUtils.getDataPoint2(j, chartData, size.width, hGap, maxVal)
            val barSize = Size(size.width - point.x, barSeries.width)

            var xOffset = 0f

            if (i > 0) {
                xOffset = prevWidths[j]
                prevWidths[j] = barSize.width
            } else {
                prevWidths.add(barSize.width)
            }

            val rightX = barSize.width + xOffset
            val topY = startPoint.y - point.y + deduct
            val bottomY = topY + barSeries.width

            path.reset()
            path.moveTo(rightX - finalRadius, topY)
            path.quadraticBezierTo(
                rightX, topY,
                rightX, topY + finalRadius
            )
            path.lineTo(rightX, bottomY - finalRadius)
            path.quadraticBezierTo(
                rightX, bottomY,
                rightX - finalRadius, bottomY
            )
            path.lineTo(xOffset - prevBottomRadius, bottomY)
            path.quadraticBezierTo(
                xOffset, bottomY,
                xOffset, bottomY - prevBottomRadius
            )
            path.lineTo(xOffset, topY + finalRadius)
            path.quadraticBezierTo(
                xOffset, topY,
                xOffset - prevBottomRadius, topY
            )
            path.lineTo(xOffset - prevBottomRadius, topY)
            drawPath(
                path = path,
                color = barSeries.color
            )
        }

        prevBottomRadius = finalRadius
    }
}

@Preview
@Composable
fun BarChartVerticalPreview() {
    BarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = Color.White),
        axesStyle = AxesStyle(
            xName = "X Axis",
            yName = "Y Axis",
        ),
        data = arrayListOf(
            BarSeries(
                data = arrayListOf(
                    ChartData(1, 100f),
                    ChartData(2, 300f),
                    ChartData(3, 1100f),
                    ChartData(4, 200f),
                    ChartData(5, 850f),
                    ChartData(6, 400f),
                    ChartData(7, 610f)
                ),
                color = Color.Green,
                legend = Legend("Budget")
            )
        )
    )
}

@Preview
@Composable
fun BarChartVerticalStackPreview() {
    BarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = Color.White),
        axesStyle = AxesStyle(
            xName = "X Axis",
            yName = "Y Axis",
        ),
        data = arrayListOf(
            BarSeries(
                data = arrayListOf(
                    ChartData(1, 100f),
                    ChartData(2, 300f),
                    ChartData(3, 1100f),
                    ChartData(4, 200f),
                    ChartData(5, 850f),
                    ChartData(6, 400f),
                    ChartData(7, 610f)
                ),
                color = Color.Green,
                legend = Legend("Budget")
            ),
            BarSeries(
                data = arrayListOf(
                    ChartData(1, 50f),
                    ChartData(2, 350f),
                    ChartData(3, 250f),
                    ChartData(4, 200f),
                    ChartData(5, 800f),
                    ChartData(6, 500f),
                    ChartData(7, 600f)
                ),
                legend = Legend("Spent")
            )
        ),
        type = BarChartType.VERTICAL_STACKED
    )
}

@Preview
@Composable
fun BarChartHorizontalPreview() {
    BarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = Color.White),
        axesStyle = AxesStyle(
            xName = "X Axis",
            yName = "Y Axis",
        ),
        data = arrayListOf(
            BarSeries(
                data = arrayListOf(
                    ChartData(1, 100f),
                    ChartData(2, 300f),
                    ChartData(3, 1100f),
                    ChartData(4, 200f),
                    ChartData(5, 850f),
                    ChartData(6, 400f),
                    ChartData(7, 610f)
                ),
                color = Color.Green,
                legend = Legend("Budget")
            )
        ),
        type = BarChartType.HORIZONTAL
    )
}

@Preview
@Composable
fun BarChartHorizontalStackPreview() {
    BarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = Color.White),
        axesStyle = AxesStyle(
            xName = "X Axis",
            yName = "Y Axis",
        ),
        data = arrayListOf(
            BarSeries(
                data = arrayListOf(
                    ChartData(1, 100f),
                    ChartData(2, 300f),
                    ChartData(3, 1100f),
                    ChartData(4, 200f),
                    ChartData(5, 850f),
                    ChartData(6, 400f),
                    ChartData(7, 610f)
                ),
                color = Color.Green,
                legend = Legend("Budget")
            ),
            BarSeries(
                data = arrayListOf(
                    ChartData(1, 50f),
                    ChartData(2, 350f),
                    ChartData(3, 250f),
                    ChartData(4, 200f),
                    ChartData(5, 800f),
                    ChartData(6, 500f),
                    ChartData(7, 600f)
                ),
                legend = Legend("Spent")
            )
        ),
        type = BarChartType.HORIZONTAL_STACKED
    )
}