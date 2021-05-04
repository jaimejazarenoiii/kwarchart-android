package com.kwarchart.android.util

import com.kwarchart.android.model.ChartData
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

    @Test
    fun getEndAngles_isCorrect() {
        val axisValues = ChartUtils.getEndAngles(floatArrayOf(
            1050f, 500f, 2050f, 800f
        ))

        assertArrayEquals(
            arrayListOf(
                85.90909f,
                40.909092f,
                167.72728f,
                65.454544f
            ).toArray(),
            axisValues.toArray()
        )
    }

    @Test
    fun getEndAngles_isWrong() {
        val axisValues = ChartUtils.getEndAngles(floatArrayOf(
            1050f, 500f, 2050f
        ))
        val unexpectedValues = arrayListOf(
            85.90909f,
            40.909092f,
            167.72728f
        )

        axisValues.forEachIndexed { i, value ->
            assertNotEquals(unexpectedValues[i], value)
        }
    }

    @Test
    fun getDataPoint_isCorrect() {
        val offset = ChartUtils.getDataPoint(
            0,
            ChartData(1, 50f),
            438f,
            116.71429f,
            800f
        )

        assertEquals(116.71429f, offset.x)
        assertEquals(410.625f, offset.y)
    }

    @Test
    fun getDataPoint_isWrong() {
        val offset = ChartUtils.getDataPoint(
            1,
            ChartData(1, 50f),
            538f,
            116.71429f,
            800f
        )

        assertNotEquals(116.71429f, offset.x)
        assertNotEquals(410.625f, offset.y)
    }

}