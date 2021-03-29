package com.kwarchart.android

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.model.Legend

/**
 * Chart template.
 *
 * @param modifier Modifier.
 * @param xAxisName X-axis name.
 * @param yAxisName Y-axis name.
 * @param legend Legend to be displayed in the chart.
 * @param content Chart's children.
 */
@Composable
fun Chart(
    modifier: Modifier = Modifier,
    xAxisName: String? = null,
    yAxisName: String? = null,
    legend: Legend? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier) {
        if (
            legend != null &&
            (legend.position == LegendPosition.TOP_LEFT ||
            legend.position == LegendPosition.TOP ||
            legend.position == LegendPosition.TOP_RIGHT)
        ) {
            HorizontalLegends(
                modifier = Modifier
                    .align(
                        if (legend.position == LegendPosition.TOP_LEFT)
                            Alignment.Start
                        else if (legend.position == LegendPosition.TOP_LEFT)
                            Alignment.CenterHorizontally
                        else
                            Alignment.End
                    )
                    .padding(10.dp),
                legend = legend
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (legend != null && legend.position == LegendPosition.LEFT) {
                VerticalLegends(
                    modifier = Modifier.padding(10.dp),
                    legend = legend
                )
            }

            yAxisName?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            Column(
                modifier = Modifier.weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                xAxisName?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 10.dp)
                    )
                }
            }

            if (legend != null && legend.position == LegendPosition.RIGHT) {
                VerticalLegends(
                    modifier = Modifier.padding(10.dp),
                    legend = legend
                )
            }
        }

        if (
            legend != null &&
            (legend.position == LegendPosition.BOTTOM_LEFT ||
            legend.position == LegendPosition.BOTTOM ||
            legend.position == LegendPosition.BOTTOM_RIGHT)
        ) {
            HorizontalLegends(
                modifier = Modifier
                    .align(
                        if (legend.position == LegendPosition.BOTTOM_LEFT)
                            Alignment.Start
                        else if (legend.position == LegendPosition.BOTTOM)
                            Alignment.CenterHorizontally
                        else
                            Alignment.End
                    )
                    .padding(10.dp),
                legend = legend
            )
        }
    }
}

/**
 * Row of legends.
 *
 * @param modifier Modifier.
 * @param legend Legend to be displayed horizontally.
 */
@Composable
private fun HorizontalLegends(
    modifier: Modifier,
    legend: Legend
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        legend.legends.forEachIndexed { i, text ->
            Box(
                modifier = Modifier.size(10.dp).background(
                    color = legend.colors[i],
                    shape = CircleShape
                )
            )
            Text(
                text = text,
                modifier = Modifier.padding(start = 5.dp, end = 10.dp)
            )
        }
    }
}

/**
 * Column of legends.
 *
 * @param modifier Modifier.
 * @param legend Legend to be displayed vertically.
 */
@Composable
private fun VerticalLegends(
    modifier: Modifier,
    legend: Legend
) {
    Column(modifier = modifier) {
        legend.legends.forEachIndexed { i, text ->
            Box(
                modifier = Modifier.size(10.dp).background(
                    color = legend.colors[i],
                    shape = CircleShape
                )
            )
            Text(
                text = text,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

/**
 * Canvas whose origin starts at bottom-left.
 *
 * @param modifier Modifier.
 * @param block Canvas painter.
 */
@Composable
fun ChartCanvas(
    modifier: Modifier = Modifier,
    block: DrawScope.() -> Unit
) {
    Canvas(modifier = modifier) {
        scale(
            scaleX = 1f,
            scaleY = -1f,
            block = block
        )
    }
}

@Preview
@Composable
fun ChartPreview() {
    Chart(
        xAxisName = "X Axis",
        yAxisName = "Y Axis"
    ) {}
}