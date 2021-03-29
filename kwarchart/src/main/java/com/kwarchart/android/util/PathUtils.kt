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

//        if (false) {
//            // first control point
//            val x1 = p0.x + mlX
//            val y1 = p0.y + mlY
//
//            // second control point
//            mlX = (p2.x - p0.x) / 2 * smoothness // (lX,lY) is the slope of the reference line
//            mlY = (p2.y - p0.y) / 2 * smoothness
//            val x2 = p1.x - mlX
//            val y2 = p1.y - mlY
//            return arrayListOf(Offset(x1, y1), Offset(x2, y2))
//        } else if (false) {
//            // first control point
//            val d0 = sqrt((p1.x - p0.x).pow(2f) + (p1.y - p0.y).pow(2f))
//            val x1 = min(p0.x + mlX * d0, (p0.x + p1.x) / 2)
//            val y1 = p0.y + mlY * d0
//
//            // second control point
//            val d1 = sqrt((p2.x - p0.x).pow(2f) + (p2.y - p0.y).pow(2f))
//            mlX = (p2.x - p0.x) / d1 * smoothness
//            mlY = (p2.y - p0.y) / d1 * smoothness
//            val x2 = max(p1.x - mlX * d0, (p0.x + p1.x) / 2f)
//            val y2 = p1.y - mlY * d0
//
//            return arrayListOf(Offset(x1, y1), Offset(x2, y2))
//        }

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