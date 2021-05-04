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
import com.kwarchart.android.AXIS_VALUES_FONT_SIZE
import com.kwarchart.android.Chart
import com.kwarchart.android.drawAxes
import com.kwarchart.android.drawGrids
import com.kwarchart.android.enum.BarChartType
import com.kwarchart.android.enum.BarChartType.*
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.model.BarSeries
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.model.Legend
import com.kwarchart.android.util.ChartUtils

private const val X_AXIS_END_PADDING = 0.5f

/**
 * Bar chart.
 *
 * @param modifier Modifier.
 * @param data List of BarSeries which will be plotted in this chart.
 * @param type Chart type for Bar.
 * @param title Chart title.
 * @param xAxisName X-axis name.
 * @param yAxisName Y-axis name.
 * @param axesColor X and Y axes color
 * @param showAxes Displayed state of axes.
 * @param gridsColor Grids color.
 * @param showGrid Displayed state of grids.
 * @param legendPos Legend position.
 */
@Composable
fun <T> BarChart(
    modifier: Modifier = Modifier,
    data: List<BarSeries<T>>,
    type: BarChartType = VERTICAL,
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
        xAxisName = xAxisName,
        title = title,
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
                    start = (maxVal.toString().length * 8).dp,
                    top = 10.dp,
                    end = 10.dp,
                    bottom = AXIS_VALUES_FONT_SIZE.dp
                )
        ) {
            if (showGrid) {
                drawGrids(
                    maxLen,
                    gridsColor,
                    xAxisEndPadding = X_AXIS_END_PADDING,
                    showVerticalLines = false
                )
            }
            if (showAxes) {
                drawAxes(axesColor, keys, maxVal, maxLen, xAxisEndPadding = X_AXIS_END_PADDING)
            }
            data.forEachIndexed { index, element ->
                drawData(element, index, data.size, type, maxLen, maxVal)
            }
        }
    }
}

/**
 * Plot line chart data.
 *
 * @param barSeries Data to be plotted in this chart.
 * @param pos Index of "barSeries".
 * @param totalSize Total data size in BarChart.
 * @param barChartType Type of bar to draw.
 * @param maxLen Max length of all passed data in BarChart.
 * @param maxVal Max value of all passed data in BarChart.
 */
private fun <T> DrawScope.drawData(
    barSeries: BarSeries<T>,
    pos: Int,
    totalSize: Int,
    barChartType: BarChartType,
    maxLen: Int,
    maxVal: Float
) {
    val vGap = size.width / (maxLen + X_AXIS_END_PADDING)

    barSeries.data.forEachIndexed { i, chartData ->
        val point = ChartUtils.getDataPoint(i, chartData, size.height, vGap, maxVal)

        when (barChartType) {
            VERTICAL -> {
                if (totalSize < 2) {
                    // Draw a single bar
                    drawRect(
                        topLeft = Offset(point.x - barSeries.width / 2, point.y),
                        size = Size(width = barSeries.width, height = size.height - point.y),
                        color = barSeries.color
                    )
                } else {
                    // Draw 2 bars
                    if (pos == 0)
                        drawRect(
                            topLeft = Offset(point.x - barSeries.width - 1, point.y),
                            size = Size(width = barSeries.width, height = size.height - point.y),
                            color = barSeries.color
                        )
                    else {
                        drawRect(
                            topLeft = Offset(point.x + 1, point.y),
                            size = Size(width = barSeries.width, height = size.height - point.y),
                            color = barSeries.color
                        )
                    }
                }
            }
            VERTICAL_STACKED -> drawRect(
                topLeft = Offset(point.x - barSeries.width / 2, point.y),
                size = Size(width = barSeries.width, height = size.height - point.y),
                color = barSeries.color,
            )
            HORIZONTAL -> TODO()
            HORIZONTAL_STACKED -> TODO()
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
        xAxisName = "X Axis",
        yAxisName = "Y Axis",
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
        xAxisName = "X Axis",
        yAxisName = "Y Axis",
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
        type = VERTICAL_STACKED
    )
}