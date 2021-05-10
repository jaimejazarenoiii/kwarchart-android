package com.kwarchart.android.util

import androidx.compose.ui.geometry.Offset
import com.kwarchart.android.model.ChartData

object ChartUtils {

    /**
     * Get list of values to be displayed in an axis.
     *
     * @param maxVal Maximum value in the axis.
     * @param maxLen Maximum data length.
     */
    fun getAxisValues(maxVal: Float, maxLen: Int) : ArrayList<Float> {
        assert(maxVal > 0)
        assert(maxLen > 1)

        val valuePerGrid = maxVal / maxLen
        val res = arrayListOf<Float>()

        for (i in 1..maxLen) {
            res.add(valuePerGrid * i)
        }

        return res
    }

    /**
     * Get list of end angles in a circle.
     *
     * @param values Values to be fitted in a 360 angle.
     */
    fun getEndAngles(values: FloatArray) : ArrayList<Float> {
        assert(values.isNotEmpty())

        val res = arrayListOf<Float>()
        var totalVal = 0f

        values.forEach {
            totalVal += it
        }

        values.forEach {
            res.add((it / totalVal) * 360f)
        }

        return res
    }

    /**
     * Get position of data point to be plotted in chart.
     * Use this when the key is in X-axis.
     *
     * @param index Data index.
     * @param data Chart data.
     * @param canvasHeight Canvas' height.
     * @param xAxisGap X-axis pixel gap per data.
     * @param yAxisMaxVal Y-axis max value based from list of data.
     *
     * @return Data point's position in canvas.
     */
    fun <T> getDataPoint(
        index: Int,
        data: ChartData<T>,
        canvasHeight: Float,
        xAxisGap: Float,
        yAxisMaxVal: Float
    ) = Offset(
        (index + 1) * xAxisGap,
        canvasHeight - ((data.value / yAxisMaxVal) * canvasHeight)
    )

    /**
     * Get position of data point to be plotted in chart.
     * Use this when the key is in Y-axis.
     *
     * @param index Data index.
     * @param data Chart data.
     * @param canvasWidth Canvas' width.
     * @param yAxisGap Y-axis pixel gap per data.
     * @param xAxisMaxVal X-axis max value based from list of data.
     *
     * @return Data point's position in canvas.
     */
    fun <T> getDataPoint2(
        index: Int,
        data: ChartData<T>,
        canvasWidth: Float,
        yAxisGap: Float,
        xAxisMaxVal: Float
    ) = Offset(
        canvasWidth - ((data.value / xAxisMaxVal) * canvasWidth),
        (index + 1) * yAxisGap,
    )

}