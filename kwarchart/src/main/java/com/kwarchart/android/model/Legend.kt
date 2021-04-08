package com.kwarchart.android.model

import androidx.compose.ui.graphics.Color
import com.kwarchart.android.enum.LegendPosition

/**
 * Legends to be displayed in chart.
 */
data class Legend(
    /**
     * Legend position.
     */
    val position: LegendPosition,

    /**
     * Legends to be displayed.
     */
    val legends: MutableList<String> = mutableListOf(),

    /**
     * Color of data represented by the legends.
     */
    val colors: MutableList<Color> = mutableListOf()
)
