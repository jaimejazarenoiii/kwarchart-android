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
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.util.PathUtils

private var mHGap = 0f
private var mVGap = 0f
private var mMaxVal = 0f

@Preview
@Composable
fun LineChartPreview() {
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = Color.White),
        legendX = "X Axis",
        legendY = "Y Axis",
        chartData = arrayListOf(
            ChartData(1, 50f),
            ChartData(2, 350f),
            ChartData(3, 250f),
            ChartData(4, 200f),
            ChartData(5, 800f),
        ),
        lineColor = Color.Red,
        isSpline = true
    )
}

/**
 *
 */
@Composable
fun <T> LineChart(
    modifier: Modifier = Modifier,
    legendX: String,
    legendY: String,
    chartData: List<ChartData<T>>,
    lineColor: Color = Color.Blue,
    axesColor: Color = Color.Gray,
    gridsColor: Color = Color.Gray,
    isSpline: Boolean = false
) {
    Chart(
        modifier = modifier,
        legendX = legendX,
        legendY = legendY
    ) {
        ChartCanvas(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(10.dp)
        ) {
            mHGap = size.width / chartData.size
            mVGap = size.height / chartData.size
            chartData.forEach {
                if (mMaxVal < it.value) {
                    mMaxVal = it.value
                }
            }

            drawGrids(this, chartData, gridsColor)
            drawAxes(this, axesColor)
            drawData(this, chartData, lineColor, isSpline)
        }
    }
}

/**
 *
 */
private fun <T> drawGrids(
    drawScope: DrawScope,
    chartData: List<ChartData<T>>,
    color: Color
) {
    val pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(10f, 10f))

    for (i in 1..chartData.size) {
        drawScope.drawLine(
            color = color,
            start = Offset(0f, i * mVGap),
            end = Offset(drawScope.size.width, i * mVGap),
            pathEffect = pathEffect
        )
        drawScope.drawLine(
            color = color,
            start = Offset(i * mHGap, 0f),
            end = Offset(i * mHGap, drawScope.size.height),
            pathEffect = pathEffect
        )
    }
}

/**
 *
 */
private fun drawAxes(drawScope: DrawScope, color: Color) {
    drawScope.drawLine(
        color = color,
        start = Offset(0f, 0f),
        end = Offset(drawScope.size.width, 0f)
    )
    drawScope.drawLine(
        color = color,
        start = Offset(0f, 0f),
        end = Offset(0f, drawScope.size.height)
    )
}

/**
 *
 */
private fun <T> drawData(
    drawScope: DrawScope,
    chartData: List<ChartData<T>>,
    color: Color,
    isSpline: Boolean
) {
    val path = Path()

    var leftPoint = Offset(0f, 0f)

    chartData.forEachIndexed { i, data ->
        val p = Offset(
            (i + 1) * mHGap,
            (data.value / mMaxVal) * drawScope.size.height
        )

        if (!isSpline) {
            path.lineTo(p.x, p.y)
            drawScope.drawCircle(
                color = color,
                radius = 10f,
                center = p
            )
            return@forEachIndexed
        }

        val rightPointIndex = if (i + 1 < chartData.size) i + 1 else i
        val rightPoint = Offset(
            (i + 2) * mHGap,
            (chartData[rightPointIndex].value / mMaxVal) * drawScope.size.height
        )
        val controlPoints = PathUtils.calculateControlPoints2(
            leftPoint, p, rightPoint, i == 0
        )
        path.cubicTo(
            controlPoints.first().x, controlPoints.first().y,
            controlPoints.last().x, controlPoints.last().y,
            p.x, p.y
        )
        drawScope.drawCircle(
            color = color,
            radius = 10f,
            center = p
        )
//        drawScope.drawCircle(
//            color = Color.Gray,
//            radius = 10f,
//            center = Offset(controlPoints.first().x, controlPoints.first().y)
//        )
//        drawScope.drawCircle(
//            color = Color.LightGray,
//            radius = 10f,
//            center = Offset(controlPoints.last().x, controlPoints.last().y)
//        )

        leftPoint = p
    }

    drawScope.drawPath(
        path = path,
        color = color,
        style = Stroke()
    )
}