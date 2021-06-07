package com.kwarchart.android.model

import androidx.compose.ui.graphics.Color

data class PieSeries<T>(
    /**
     * Data to be plotted in PieChart.
     */
    val data: ChartData<T>,

    /**
     * Color(s) of the pie series.
     *
     * Set as list because gradient is supported.
     */
    val colors: List<Color> = arrayListOf(Color.Black),

    /**
     * Legend for the pie series.
     */
    val legend: Legend
)