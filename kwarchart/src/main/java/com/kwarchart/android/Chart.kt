package com.kwarchart.android

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwarchart.android.chart.*
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.model.AxesStyle
import com.kwarchart.android.model.FontStyle
import com.kwarchart.android.model.Legend
import com.kwarchart.android.util.ChartUtils

private const val WEIGHT_CANVAS_AREA = 2f
private const val WEIGHT_X_AXIS_NAME = 1f

/**
 * Chart template.
 *
 * @param modifier Modifier.
 * @param title Chart title.
 * @param axesStyle X and Y axes style.
 * @param legend Legend to be displayed in the chart.
 * @param content Chart's children.
 */
@Composable
fun Chart(
    modifier: Modifier = Modifier,
    title: String? = null,
    axesStyle: AxesStyle = AxesStyle(),
    legend: Legend? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        title?.let {
            Text(it)
        }

        if (legend != null) TopLegends(legend)

        Row(
            modifier = Modifier.fillMaxWidth().weight(WEIGHT_CANVAS_AREA),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (legend != null) LeftLegends(legend)

            axesStyle.yName?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            Column(
                modifier = Modifier.weight(WEIGHT_CANVAS_AREA),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                axesStyle.xName?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .weight(WEIGHT_X_AXIS_NAME)
                            .padding(bottom = 10.dp)
                    )
                }
            }

            if (legend != null) RightLegends(legend)
        }

        if (legend != null) BottomLegends(legend)
    }
}

/**
 * Plot legends at the top of the chart.
 *
 * @param legend Legend to be plotted.
 */
@Composable
private fun ColumnScope.TopLegends(legend: Legend) {
    if (
        legend.position == LegendPosition.TOP_LEFT ||
        legend.position == LegendPosition.TOP ||
        legend.position == LegendPosition.TOP_RIGHT
    ) {
        HorizontalLegends(
            modifier = Modifier
                .align(
                    if (legend.position == LegendPosition.TOP_LEFT)
                        Alignment.Start
                    else if (legend.position == LegendPosition.TOP)
                        Alignment.CenterHorizontally
                    else
                        Alignment.End
                )
                .padding(10.dp),
            legend = legend
        )
    }
}

/**
 * Plot legends at the left of the chart.
 *
 * @param legend Legend to be plotted.
 */
@Composable
private fun LeftLegends(legend: Legend) {
    if (legend.position == LegendPosition.LEFT) {
        VerticalLegends(
            modifier = Modifier.padding(10.dp),
            legend = legend
        )
    }
}

/**
 * Plot legends at the right of the chart.
 *
 * @param legend Legend to be plotted.
 */
@Composable
private fun RightLegends(legend: Legend) {
    if (legend.position == LegendPosition.RIGHT) {
        VerticalLegends(
            modifier = Modifier.padding(10.dp),
            legend = legend
        )
    }
}

/**
 * Plot legends at the bottom of the chart.
 *
 * @param legend Legend to be plotted.
 */
@Composable
private fun ColumnScope.BottomLegends(legend: Legend) {
    if (
        legend.position == LegendPosition.BOTTOM_LEFT ||
        legend.position == LegendPosition.BOTTOM ||
        legend.position == LegendPosition.BOTTOM_RIGHT
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
            Row(verticalAlignment = Alignment.CenterVertically) {
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

/**
 * Canvas' origin point.
 *
 * @return Offset point.
 */
fun DrawScope.origin() = Offset(0f, size.height)

/**
 * Draw chart grids.
 *
 * @param count Grid count.
 * @param color Grid color.
 * @param xAxisEndPadding X-axis end padding.
 * @param showHorizontalLines Show/Hide horizontal lines.
 * @param showVerticalLines Show/Hide vertical lines.
 */
fun DrawScope.drawGrids(
    count: Int,
    color: Color,
    xAxisEndPadding: Float = 0f,
    yAxisEndPadding: Float = 0f,
    showHorizontalLines: Boolean = true,
    showVerticalLines: Boolean = true,
) {
    val hGap = (size.height - yAxisEndPadding) / count
    val vGap = (size.width - xAxisEndPadding) / count
    val startPoint = origin()
//    val pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(10f, 10f))

    for (i in 1..count) {
        if (showHorizontalLines) {
            // Horizontal lines
            drawLine(
                color = color,
                start = Offset(0f, startPoint.y - i * hGap),
                end = Offset(size.width, startPoint.y - i * hGap),
//            pathEffect = pathEffect
            )
        }

        if (showVerticalLines) {
            // Vertical lines
            drawLine(
                color = color,
                start = Offset(i * vGap, 0f),
                end = Offset(i * vGap, size.height),
//            pathEffect = pathEffect
            )
        }
    }
}

/**
 * Draw X and Y axes.
 *
 * @param color Axes color.
 * @param keys Keys to be plotted in the X-axis.
 * @param maxVal Value axis' max value.
 * @param maxLen Value axis' max length.
 * @param xAxisEndPadding X-axis end padding.
 */
fun <T> DrawScope.drawAxes(
    axesStyle: AxesStyle,
    keys: List<T>,
    maxVal: Float,
    maxLen: Int,
    xAxisEndPadding: Float = 0f,
    yAxisEndPadding: Float = 0f,
    reverseKeyVal: Boolean = false
) {
    val hGap = (size.height - yAxisEndPadding) / keys.size
    val vGap = (size.width - xAxisEndPadding) / maxLen
    val startPoint = origin()

    // X-axis
    if (axesStyle.xStyle.show) {
        drawLine(
            strokeWidth = axesStyle.xStyle.strokeWidth,
            color = axesStyle.xStyle.color,
            start = startPoint,
            end = Offset(size.width, startPoint.y),
            cap = StrokeCap.Square,
            pathEffect = axesStyle.xStyle.strokeStyle
        )
    }
    // Y-axis
    if (axesStyle.yStyle.show) {
        drawLine(
            strokeWidth = axesStyle.yStyle.strokeWidth,
            color = axesStyle.yStyle.color,
            start = startPoint,
            end = Offset(0f, 0f),
            cap = StrokeCap.Square,
            pathEffect = axesStyle.yStyle.strokeStyle
        )
    }

    if (!axesStyle.xStyle.show && !axesStyle.yStyle.show) {
        return
    }

    drawIntoCanvas {
        val xTextPaint = createAxisTextPaint(axesStyle.xValueFontStyle)
        val yTextPaint = createAxisTextPaint(
            axesStyle.yValueFontStyle, Paint.Align.RIGHT
        )

        ChartUtils.getAxisValues(maxVal, maxLen).forEachIndexed { i, value ->
            if (axesStyle.xStyle.show) {
                val xOffset = Offset(
                    (i + 1) * vGap,
                    startPoint.y + axesStyle.xValueFontStyle.size + 10f
                )
                it.nativeCanvas.drawText(
                    if (reverseKeyVal) value.toInt().toString() else keys[i].toString(),
                    xOffset.x,
                    xOffset.y,
                    xTextPaint
                )
            }

            if (axesStyle.yStyle.show) {
                val yOffset = Offset(
                    -20f,
                    (startPoint.y - (i + 1) * hGap) + axesStyle.yValueFontStyle.size / 2
                )
                it.nativeCanvas.drawText(
                    if (reverseKeyVal) keys[i].toString() else value.toInt().toString(),
                    yOffset.x,
                    yOffset.y,
                    yTextPaint
                )
            }
        }
    }
}

/**
 * Create a paint for drawing text.
 *
 * @param fontStyle Text font style.
 * @param textAlign Text alignment.
 */
private fun createAxisTextPaint(
    fontStyle: FontStyle,
    textAlign: Paint.Align = Paint.Align.CENTER
) = Paint(Paint.ANTI_ALIAS_FLAG).apply{
    this.textAlign = textAlign
    this.textSize = fontStyle.size
    this.color = fontStyle.color.toArgb()
    this.isFakeBoldText = fontStyle.weight == FontWeight.Bold
}

@Preview
@Composable
fun ChartPreview() {
    Chart(
        axesStyle = AxesStyle(
            xName = "X Axis",
            yName = "Y Axis"
        )
    ) {}
}