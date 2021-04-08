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

}