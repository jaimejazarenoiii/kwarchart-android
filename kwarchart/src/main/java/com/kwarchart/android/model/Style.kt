package com.kwarchart.android.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect

data class Style(
    val color: Color = Color.Gray,
    val show: Boolean = true,
    val strokeWidth: Float = 1f,
    val strokeStyle: PathEffect? = null
)

data class AxesStyle(
    val xName: String? = null,
    val yName: String? = null,
    val xStyle: Style = Style(),
    val yStyle: Style = Style()
)

data class GridsStyle(
    val horizontal: Style = Style(),
    val vertical: Style = Style()
)