package com.kwarchart.android.model

import androidx.compose.ui.graphics.Color

data class PieSeries<T>(
    /**
     * Data to be plotted in PieChart.
     */
    val data: ChartData<T>,

    /**
     * Color of the pie series.
     */
    val color: Color = Color.Black,

    /**
     * Legend for the pie series.
     */
    val legend: String
)