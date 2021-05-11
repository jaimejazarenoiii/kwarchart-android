package com.kwarchart.android.chart

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwarchart.android.Chart
import com.kwarchart.android.ChartCanvas
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.enum.PieChartType
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.model.Legend
import com.kwarchart.android.model.PieSeries
import com.kwarchart.android.util.ChartUtils
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private lateinit var mEndAngles: ArrayList<Float>

/**
 * Pie chart.
 *
 * @param modifier Modifier.
 * @param data List of LineSeries which will be plotted in this chart.
 * @param type
 * @param title
 * @param legendPos Legend position.
 */
@Composable
fun <T> PieChart(
    modifier: Modifier = Modifier,
    data: List<PieSeries<T>>,
    type: PieChartType = PieChartType.NORMAL,
    title: String? = null,
    legendPos: LegendPosition? = null
) {
    mEndAngles = ChartUtils.getEndAngles(data.map { it.data.value }.toFloatArray())

    Chart(
        modifier = modifier,
        title = title,
        legend = legendPos?.let { _ ->
            val tmpLegend = Legend(legendPos)
            data.forEach {
                tmpLegend.legends.add(it.legend)
                tmpLegend.colors.add(it.color)
            }
            tmpLegend
        }
    ) {
        ChartCanvas(modifier = modifier.padding(10.dp)) {
            if (type == PieChartType.NORMAL) {
                drawArcs(data)
            } else {
                drawDoughnutArcs(data)
            }

            // TODO: To be implemented in the future.
//            drawArcValues(data)
        }
    }
}

/**
 * Draw pie chart arcs.
 *
 * @param data Data to be plotted in this chart.
 */
private fun <T> DrawScope.drawArcs(data: List<PieSeries<T>>) {
    var startAngle = 0f

    for (i in data.indices) {
        val endAngle = mEndAngles[i]

        drawArc(
            startAngle = -startAngle,
            sweepAngle = -endAngle,
            color = data[i].color,
            useCenter = true
        )

        startAngle += endAngle
    }
}

/**
 * Draw pie chart doughnut arcs.
 *
 * @param data Data to be plotted in this chart.
 */
private fun <T> DrawScope.drawDoughnutArcs(data: List<PieSeries<T>>) {
    drawArcs(data)
    drawOval(
        color = Color.White,
        topLeft = Offset((size.width * 0.5f) / 2, (size.height * 0.5f) / 2),
        size = Size(size.width * 0.5f, size.height * 0.5f)
    )
}

/**
 * Draw pie chart arcs' values.
 *
 * @param data Data to be plotted in this chart.
 */
private fun <T> DrawScope.drawArcValues(data: List<PieSeries<T>>) {
    var startAngle = 0f

    val textPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 64f
        color = 0xffffffff.toInt()
    }
//    val origin = Offset(
//        this.size.width / 2f,
//        this.size.height / 2f
//    )
    val majorAxis = this.size.width / 2f
    val minorAxis = this.size.height / 2f
    var startXY = Offset(majorAxis * 2f, -minorAxis)

    drawIntoCanvas {
        it.nativeCanvas.scale(1f, -1f)

        for (i in data.indices) {
            val endAngle = mEndAngles[i] * PI.toFloat() /180

            val x = cos(endAngle) * majorAxis
            val y = -(sin(endAngle) * minorAxis)

            startXY = Offset(
                majorAxis + ((startXY.x - majorAxis - x) / 2f),
                -((minorAxis - (minorAxis + y)) / 2f)
            )

            it.nativeCanvas.drawText(
                data[i].data.value.toString(),
                startXY.x,
                startXY.y,
                textPaint
            )

            startAngle += endAngle
        }
    }
}

@Preview
@Composable
fun PieChartNormalPreview() {
    PieChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = Color.White),
        data = arrayListOf(
            PieSeries(
                data = ChartData("Bills", 1050f),
                color = Color.Red,
                legend = "Bills"
            ),
            PieSeries(
                data = ChartData("Shopping", 500f),
                color = Color.Green,
                legend = "Shopping"
            ),
            PieSeries(
                data = ChartData("Food", 2050f),
                color = Color.Blue,
                legend = "Food"
            ),
            PieSeries(
                data = ChartData("Transportation", 800f),
                color = Color.Yellow,
                legend = "Transportation"
            ),
        ),
        title = "This month's transactions",
        legendPos = LegendPosition.RIGHT
    )
}

@Preview
@Composable
fun PieChartDoughnutPreview() {
    PieChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = Color.White),
        data = arrayListOf(
            PieSeries(
                data = ChartData("Bills", 1050f),
                color = Color.Red,
                legend = "Bills"
            ),
            PieSeries(
                data = ChartData("Shopping", 500f),
                color = Color.Green,
                legend = "Shopping"
            ),
            PieSeries(
                data = ChartData("Food", 2050f),
                color = Color.Blue,
                legend = "Food"
            ),
            PieSeries(
                data = ChartData("Transportation", 800f),
                color = Color.Yellow,
                legend = "Transportation"
            ),
        ),
        type = PieChartType.DOUGHNUT,
        title = "This month's transactions",
        legendPos = LegendPosition.RIGHT
    )
}