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
     * Color(s) of the bar series.
     *
     * Set as list because gradient is supported.
     */
    val colors: List<Color> = arrayListOf(Color.Black),

    /**
     * Width of the bar series.
     */
    val width: Float = 20f,

    /**
     * Bar corner radius.
     */
    val radius: Float = 0f,

    /**
     * Legend for the bar series.
     */
    val legend: Legend
)