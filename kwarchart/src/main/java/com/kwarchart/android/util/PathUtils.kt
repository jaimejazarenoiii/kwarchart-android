package com.kwarchart.android.util

import androidx.compose.ui.geometry.Offset
import com.kwarchart.android.model.ControlPoints

object PathUtils {

    private var mlX = 0f
    private var mlY = 0f

    /**
     * Calculate control points of a cubic bezier curve.
     *
     * @param p0 First point.
     * @param p1 Second/Middle point.
     * @param p2 Third point.
     * @param isFirstPoint Set to "true" to reset reference slopes.
     * @param smoothness Curve smoothness.
     *
     * @return Cubic bezier curve's control points.
     */
    fun calculateControlPoints(
        p0: Offset,
        p1: Offset,
        p2: Offset,
        isFirstPoint: Boolean,
        smoothness: Float = 0.3f,
    ) : ControlPoints {
        if (isFirstPoint) {
            mlX = 0f
            mlY = 0f
        }

        val m = (p2.y - p0.y) / (p2.x - p0.x)
        val dx2 = (p2.x - p1.x) * -smoothness
        val dy2 = dx2 * m
        val res = ControlPoints(
            x1 = p0.x - mlX, y1 = p0.y - mlY,
            x2 = p1.x + dx2, y2 = p1.y + dy2
        )

        mlX = dx2
        mlY = dy2

        return res
    }

}