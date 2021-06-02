package com.kwarchart.android

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.sp
import com.kwarchart.android.chart.*
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.model.AxesStyle
import com.kwarchart.android.model.FontStyle
import com.kwarchart.android.model.GridsStyle
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
 * @param legends Legends to be displayed in the chart.
 * @param legendColors Legend colors.
 * @param legendPos Legend position.
 * @param content Chart's children.
 */
@Composable
fun Chart(
    modifier: Modifier = Modifier,
    title: String? = null,
    axesStyle: AxesStyle = AxesStyle(),
    legends: List<Legend>? = null,
    legendColors: List<Color>? = null,
    legendPos: LegendPosition? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        title?.let {
            Text(it)
        }

        if (legendPos != null) TopLegends(legends!!, legendColors!!, legendPos)

        Row(
            modifier = Modifier.fillMaxWidth().weight(WEIGHT_CANVAS_AREA),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (legendPos != null) LeftLegends(legends!!, legendColors!!, legendPos)

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

            if (legendPos != null) RightLegends(legends!!, legendColors!!, legendPos)
        }

        if (legendPos != null) BottomLegends(legends!!, legendColors!!, legendPos)
    }
}

/**
 * Plot legends at the top of the chart.
 *
 * @param legends Legends to be plotted.
 * @param legendColors Legend colors.
 * @param legendPos Legend position.
 */
@Composable
private fun ColumnScope.TopLegends(
    legends: List<Legend>,
    legendColors: List<Color>,
    legendPos: LegendPosition
) {
    if (
        legendPos == LegendPosition.TOP_LEFT ||
        legendPos == LegendPosition.TOP ||
        legendPos == LegendPosition.TOP_RIGHT
    ) {
        HorizontalLegends(
            modifier = Modifier
                .align(
                    if (legendPos == LegendPosition.TOP_LEFT)
                        Alignment.Start
                    else if (legendPos == LegendPosition.TOP)
                        Alignment.CenterHorizontally
                    else
                        Alignment.End
                )
                .padding(10.dp),
            legends = legends,
            legendColors = legendColors
        )
    }
}

/**
 * Plot legends at the left of the chart.
 *
 * @param legends Legends to be plotted.
 * @param legendColors Legend colors.
 * @param legendPos Legend position.
 */
@Composable
private fun LeftLegends(
    legends: List<Legend>,
    legendColors: List<Color>,
    legendPos: LegendPosition
) {
    if (legendPos == LegendPosition.LEFT) {
        VerticalLegends(
            modifier = Modifier.padding(10.dp),
            legends = legends,
            legendColors = legendColors
        )
    }
}

/**
 * Plot legends at the right of the chart.
 *
 * @param legends Legends to be plotted.
 * @param legendColors Legend colors.
 * @param legendPos Legend position.
 */
@Composable
private fun RightLegends(
    legends: List<Legend>,
    legendColors: List<Color>,
    legendPos: LegendPosition
) {
    if (legendPos == LegendPosition.RIGHT) {
        VerticalLegends(
            modifier = Modifier.padding(10.dp),
            legends = legends,
            legendColors = legendColors
        )
    }
}

/**
 * Plot legends at the bottom of the chart.
 *
 * @param legends Legends to be plotted.
 * @param legendColors Legend colors.
 * @param legendPos Legend position.
 */
@Composable
private fun ColumnScope.BottomLegends(
    legends: List<Legend>,
    legendColors: List<Color>,
    legendPos: LegendPosition
) {
    if (
        legendPos == LegendPosition.BOTTOM_LEFT ||
        legendPos == LegendPosition.BOTTOM ||
        legendPos == LegendPosition.BOTTOM_RIGHT
    ) {
        HorizontalLegends(
            modifier = Modifier
                .align(
                    if (legendPos == LegendPosition.BOTTOM_LEFT)
                        Alignment.Start
                    else if (legendPos == LegendPosition.BOTTOM)
                        Alignment.CenterHorizontally
                    else
                        Alignment.End
                )
                .padding(10.dp),
            legends = legends,
            legendColors = legendColors
        )
    }
}

/**
 * Row of legends.
 *
 * @param modifier Modifier.
 * @param legends Legends to be displayed horizontally.
 * @param legendColors Legend colors.
 */
@Composable
private fun HorizontalLegends(
    modifier: Modifier,
    legends: List<Legend>,
    legendColors: List<Color>
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        legends.forEachIndexed { i, legend ->
            Box(
                modifier = Modifier.size(10.dp).background(
                    color = legendColors[i],
                    shape = legend.shape
                )
            )
            Text(
                text = legend.text,
                color = legend.fontStyle.color,
                fontSize = legend.fontStyle.size.sp,
                fontWeight = legend.fontStyle.weight,
                modifier = Modifier.padding(start = 5.dp, end = 10.dp)
            )
        }
    }
}

/**
 * Column of legends.
 *
 * @param modifier Modifier.
 * @param legends Legends to be displayed vertically.
 * @param legendColors Legend colors.
 */
@Composable
private fun VerticalLegends(
    modifier: Modifier,
    legends: List<Legend>,
    legendColors: List<Color>
) {
    Column(modifier = modifier) {
        legends.forEachIndexed { i, legend ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(10.dp).background(
                        color = legendColors[i],
                        shape = legend.shape
                    )
                )
                Text(
                    text = legend.text,
                    color = legend.fontStyle.color,
                    fontSize = legend.fontStyle.size.sp,
                    fontWeight = legend.fontStyle.weight,
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
 * @param gridsStyle Grid style.
 * @param count Grid count.
 * @param xAxisEndPadding X-axis end padding.
 * @param yAxisEndPadding X-axis end padding.
 */
fun DrawScope.drawGrids(
    gridsStyle: GridsStyle,
    count: Int,
    xAxisEndPadding: Float = 0f,
    yAxisEndPadding: Float = 0f
) {
    if (!gridsStyle.horizontal.show && !gridsStyle.vertical.show) {
        return
    }

    val hGap = (size.height - yAxisEndPadding) / count
    val vGap = (size.width - xAxisEndPadding) / count
    val startPoint = origin()

    for (i in 1..count) {
        if (gridsStyle.horizontal.show) {
            // Horizontal lines
            drawLine(
                strokeWidth = gridsStyle.horizontal.strokeWidth,
                color = gridsStyle.horizontal.color,
                start = Offset(0f, startPoint.y - i * hGap),
                end = Offset(size.width, startPoint.y - i * hGap),
                pathEffect = gridsStyle.horizontal.strokeStyle
            )
        }

        if (gridsStyle.vertical.show) {
            // Vertical lines
            drawLine(
                strokeWidth = gridsStyle.vertical.strokeWidth,
                color = gridsStyle.vertical.color,
                start = Offset(i * vGap, 0f),
                end = Offset(i * vGap, size.height),
                pathEffect = gridsStyle.vertical.strokeStyle
            )
        }
    }
}

/**
 * Draw X and Y axes.
 *
 * @param axesStyle Axes style.
 * @param keys Keys to be plotted in the X-axis.
 * @param maxVal Value axis' max value.
 * @param maxLen Value axis' max length.
 * @param xAxisEndPadding X-axis end padding.
 * @param yAxisEndPadding Y-axis end padding.
 * @param reverseKeyVal If TRUE, "key" is displayed in Y-axis and value in X-axis.
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