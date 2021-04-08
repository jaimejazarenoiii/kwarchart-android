package com.kwarchart.android.model

/**
 * Control points of a cubic bezier curve.
 */
data class ControlPoints(
    /**
     * First control point's X coordinate.
     */
    val x1: Float,

    /**
     * First control point's Y coordinate.
     */
    val y1: Float,

    /**
     * Second control point's X coordinate.
     */
    val x2: Float,

    /**
     * Second control point's Y coordinate.
     */
    val y2: Float
)
