package com.kwarchart.android.model

import androidx.compose.ui.graphics.Color
import com.kwarchart.android.enum.BarChartType

/**
 * LineChart data.
 */
data class BarSeries<T>(
        /**
     * Data to be plotted in LineChart.
     */
    val data: List<ChartData<T>>,

        /**
     * Type of LineChart for this line series.
     */
    val type: BarChartType = BarChartType.NORMAL,

        /**
     * Color of the bar series.
     */
    val color: Color = Color.Black,

        /**
     * Width of the bar series.
     */
    val width: Int = 5,

        /**
     * Color of the area in the bar series.
     */
    val areaColor: Color? = Color.Black,

        /**
     * Legend for the bar series.
     */
    val legend: String
)