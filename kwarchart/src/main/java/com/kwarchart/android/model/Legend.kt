package com.kwarchart.android.model

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Shape

/**
 * Legends to be displayed in chart.
 */
data class Legend(
    /**
     * Legend text.
     */
    val text: String,

    /**
     * Legends shape.
     */
    val shape: Shape = CircleShape,

    /**
     * Font style.
     */
    val fontStyle: FontStyle = FontStyle(14f)
)
