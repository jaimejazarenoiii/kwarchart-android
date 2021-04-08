package com.kwarchart.android.util

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

}