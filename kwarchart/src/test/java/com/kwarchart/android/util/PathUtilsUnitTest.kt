package com.kwarchart.android.util

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.*
import org.junit.Test

class PathUtilsUnitTest {

    @Test
    fun calculateControlPoints_isCorrect() {
        val cp1 = PathUtils.calculateControlPoints(
            p0 = Offset(0.0f, 601.0f),
            p1 = Offset(134.57143f, 563.4375f),
            p2 = Offset(269.14285f, 338.0625f),
            isFirstPoint = true
        )
        val cp2 = PathUtils.calculateControlPoints(
            p0 = Offset(134.57143f, 563.4375f),
            p1 = Offset(269.14285f, 338.0625f),
            p2 = Offset(403.7143f, 413.1875f),
            isFirstPoint = false
        )
        val cp3 = PathUtils.calculateControlPoints(
            p0 = Offset(269.14285f, 338.0625f),
            p1 = Offset(403.7143f, 413.1875f),
            p2 = Offset(538.2857f, 450.75f),
            isFirstPoint = false
        )

        assertEquals(0.0f, cp1.x1)
        assertEquals(601.0f, cp1.y1)
        assertEquals(94.2f, cp1.x2)
        assertEquals(602.8781f, cp1.y2)

        assertEquals(174.94286f, cp2.x1)
        assertEquals(523.9969f, cp2.y1)
        assertEquals(228.77142f, cp2.x2)
        assertEquals(360.6f, cp2.y2)

        assertEquals(309.51428f, cp3.x1)
        assertEquals(315.525f, cp3.y1)
        assertEquals(363.34286f, cp3.x2)
        assertEquals(396.28436f, cp3.y2)
    }

}