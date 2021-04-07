package com.kwarchart.android.chart

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwarchart.android.Chart
import com.kwarchart.android.ChartCanvas
import com.kwarchart.android.model.ChartData
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private var mTotalVal = 0f

/**
 *
 * @param modifier
 */
@Composable
fun <T> PieChart(
    modifier: Modifier,
    legendX: String,
    legendY: String,
    chartData: List<ChartData<T>>,
) {
    mTotalVal = 0f

    Chart(
        modifier = modifier,
        xAxisName = legendX,
        yAxisName = legendY
    ) {
        ChartCanvas(modifier = modifier.padding(10.dp)) {
            val size = chartData.size - 1
            chartData.forEach {
                mTotalVal += it.value
            }
            val textPaint = Paint()
            textPaint.textAlign = Paint.Align.CENTER
            textPaint.textSize = 64f
            textPaint.color = 0xffffffff.toInt()

            var startAngle = 0f

            for (i in 0..size) {
                val endAngle = (chartData[i].value / mTotalVal) * 360f

                drawArc(
                    startAngle = -startAngle,
                    sweepAngle = -endAngle,
                    color = Color(
                        Random.nextInt(255),
                        Random.nextInt(255),
                        Random.nextInt(255),
                    ),
                    useCenter = true
                )

                startAngle += endAngle
            }

            drawIntoCanvas {
                val origin = Offset(
                    this.size.width / 2f,
                    this.size.height / 2f
                )
                val majorAxis = this.size.width / 2f
                val minorAxis = this.size.height / 2f
                var startXY = Offset(majorAxis * 2f, -minorAxis)
                startAngle = 0f

                for (i in 0..size) {
                    val endAngle = (chartData[i].value / mTotalVal) * (PI.toFloat() * 2f)

                    if (i == 0) {
                        it.nativeCanvas.scale(1f, -1f)
                    }


                    val x = cos(endAngle) * majorAxis
                    val y = -(sin(endAngle) * minorAxis)

                    Log.d("SHIT", "value = ${chartData[i].value}")
                    Log.d("SHIT", "endAngle = $endAngle  :  x = $x  :  y = $y")


                    startXY = Offset(
                        majorAxis + ((startXY.x - majorAxis - x) / 2f),
                        -((minorAxis - (minorAxis + y)) / 2f)
                    )
//                    if (i == 0)
                    it.nativeCanvas.drawText(
                        chartData[i].value.toString(),
                        startXY.x,
                        startXY.y,
                        textPaint
                    )

                    startAngle += endAngle
                }
            }
        }
    }
}

@Preview
@Composable
fun PieChartPreview() {
//    PieChart()
}