package com.kwarchart.android.util

import org.junit.Assert.*
import org.junit.Test

class ChartUtilsUnitTest {

    @Test
    fun getAxisValues_isCorrect() {
        val axisValues = ChartUtils.getAxisValues(800f, 7)

        assertArrayEquals(
            arrayListOf(
                114.28571f,
                228.57143f,
                342.85715f,
                457.14285f,
                571.4286f,
                685.7143f,
                800.0f
            ).toArray(),
            axisValues.toArray()
        )
    }

    @Test
    fun getAxisValues_isWrong() {
        val axisValues = ChartUtils.getAxisValues(800f, 2)
        val unexpectedValues = arrayListOf(114.28571f, 228.57143f)

        axisValues.forEachIndexed { i, value ->
            assertNotEquals(unexpectedValues[i], value)
        }
    }

}