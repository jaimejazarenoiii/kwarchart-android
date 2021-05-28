package com.kwarchart.android.chart

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
        BarChartType.VERTICAL, BarChartType.HORIZONTAL -> maxVals.max()!!

        BarChartType.VERTICAL_STACKED, BarChartType.HORIZONTAL_STACKED -> maxVals.sum()
    }

    Chart(
        modifier = modifier,
        title = title,
        axesStyle = axesStyle,
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
                    start = (maxVal.toString().length * 8).dp,
                    top = 10.dp,
                    end = 10.dp,
                    bottom = AXIS_VALUES_FONT_SIZE.dp
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
    val halfDataSize = data.size / 2

    data.forEachIndexed { i, barSeries ->
        val vGap = (size.width - xAxisEndPadding) / maxLen

        var deduct: Float

        if (data.size % 2 == 0) {
            deduct = (i - halfDataSize) * barSeries.width
        } else {
            if (i == halfDataSize) {
                deduct = -barSeries.width / 2
            } else {
                deduct = (i - halfDataSize) * barSeries.width
                deduct -= barSeries.width / 2
            }
        }

        barSeries.data.forEachIndexed { j, chartData ->
            val point = ChartUtils.getDataPoint(j, chartData, size.height, vGap, maxVal)

            drawRect(
                topLeft = Offset(point.x + deduct, point.y),
                size = Size(barSeries.width, size.height - point.y),
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
    val prevHeights = arrayListOf<Float>()

    data.forEachIndexed { i, barSeries ->
        val vGap = (size.width - xAxisEndPadding) / maxLen
        val deduct = -barSeries.width / 2

        barSeries.data.forEachIndexed { j, chartData ->
            val point = ChartUtils.getDataPoint(j, chartData, size.height, vGap, maxVal)
            val barSize = Size(barSeries.width, size.height - point.y)

            var yOffset = 0f

            if (i > 0) {
                yOffset = prevHeights[j]
                prevHeights[j] = barSize.height
            } else {
                prevHeights.add(barSize.height)
            }

            drawRect(
                topLeft = Offset(point.x + deduct, point.y - yOffset),
                size = barSize,
                color = barSeries.color
            )
        }
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
    val startPoint = origin()
    val halfDataSize = data.size / 2

    data.forEachIndexed { i, barSeries ->
        val hGap = (size.height - yAxisEndPadding) / maxLen

        var deduct: Float

        if (data.size % 2 == 0) {
            deduct = (i - halfDataSize) * barSeries.width
        } else {
            if (i == halfDataSize) {
                deduct = -barSeries.width / 2
            } else {
                deduct = (i - halfDataSize) * barSeries.width
                deduct -= barSeries.width / 2
            }
        }

        barSeries.data.forEachIndexed { j, chartData ->
            val point = ChartUtils.getDataPoint2(j, chartData, size.width, hGap, maxVal)

            drawRect(
                topLeft = Offset(1f, startPoint.y - point.y + deduct),
                size = Size(size.width - point.x, barSeries.width),
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
    val startPoint = origin()
    val prevWidths = arrayListOf<Float>()

    data.forEachIndexed { i, barSeries ->
        val hGap = (size.height - yAxisEndPadding) / maxLen
        val deduct = -barSeries.width / 2

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

            drawRect(
                topLeft = Offset(1f + xOffset, startPoint.y - point.y + deduct),
                size = barSize,
                color = barSeries.color
            )
        }
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
                legend = "Budget"
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
                legend = "Budget"
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
                legend = "Spent"
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
                legend = "Budget"
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
                legend = "Budget"
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
                legend = "Spent"
            )
        ),
        type = BarChartType.HORIZONTAL_STACKED
    )
}