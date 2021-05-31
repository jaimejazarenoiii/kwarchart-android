package com.kwarchart.android.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight

/**
 * Common style.
 */
data class Style(
    /**
     * Color.
     */
    val color: Color = Color.Gray,

    /**
     * Visibility.
     */
    val show: Boolean = true,

    /**
     * Stroke width/thickness.
     */
    val strokeWidth: Float = 1f,

    /**
     * Stroke style/effect.
     */
    val strokeStyle: PathEffect? = null
)

/**
 * Font style.
 */
data class FontStyle(
    /**
     * Font size.
     */
    val size: Float = 32f,

    /**
     * Color.
     */
    val color: Color = Color.Black,

    /**
     * Font weight.
     */
    val weight: FontWeight? = null
)

/**
 * Axes style.
 */
data class AxesStyle(
    /**
     * X-axis name/text.
     */
    val xName: String? = null,

    /**
     * Y-axis name/text.
     */
    val yName: String? = null,

    /**
     * X-axis style.
     */
    val xStyle: Style = Style(),

    /**
     * Y-axis style.
     */
    val yStyle: Style = Style(),

    /**
     * X-axis font style
     */
    val xValueFontStyle: FontStyle = FontStyle(),

    /**
     * Y-axis font style.
     */
    val yValueFontStyle: FontStyle = FontStyle()
)

data class GridsStyle(
    /**
     * Horizontal grid style.
     */
    val horizontal: Style = Style(),
  
    /**
     * Vertical grid style.
     */
    val vertical: Style = Style()
)