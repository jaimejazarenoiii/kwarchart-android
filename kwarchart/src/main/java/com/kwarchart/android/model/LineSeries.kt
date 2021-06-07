package com.kwarchart.android.model

import androidx.compose.ui.graphics.Color
import com.kwarchart.android.enum.LineChartType

/**
 * LineChart data.
 */
data class LineSeries<T>(
    /**
     * Data to be plotted in LineChart.
     */
    val data: List<ChartData<T>>,

    /**
     * Type of LineChart for this line series.
     */
    val type: LineChartType = LineChartType.NORMAL,

    /**
     * Color of the line series.
     */
    val color: Color = Color.Black,

    /**
     * Width of the line series.
     */
    val width: Int = 5,

    /**
     * Color of the data point in the line series.
     */
    val dataPointColor: Color = color,

    /**
     * Data point displayed state.
     */
    val showDataPoint: Boolean = false,

    /**
     * Color of the area in the line series.
     */
    val areaColor: Color? = null,

    /**
     * Legend for the line series.
     */
    val legend: Legend
)
