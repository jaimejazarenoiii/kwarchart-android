package com.kwarchart.android.model

import androidx.compose.ui.graphics.Color

/**
 * BarChart data.
 */
data class BarSeries<T>(
    /**
     * Data to be plotted in LineChart.
     */
    val data: List<ChartData<T>>,

    /**
     * Color of the bar series.
     */
    val color: Color = Color.Black,

    /**
     * Width of the bar series.
     */
    val width: Float = 20f,

    /**
     * Legend for the bar series.
     */
    val legend: String,
)