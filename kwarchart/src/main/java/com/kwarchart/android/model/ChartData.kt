package com.kwarchart.android.model

/**
 * Data to be plotted in chart.
 */
data class ChartData<T>(
    /**
     * Key.
     */
    val key: T,

    /**
     * Value.
     */
    val value: Float
)
