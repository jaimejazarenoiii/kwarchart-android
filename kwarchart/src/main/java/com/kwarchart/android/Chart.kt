package com.kwarchart.android

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *
 */
@Composable
fun Chart(
    modifier: Modifier = Modifier,
    legendX: String,
    legendY: String,
    legendColor: Color = Color.Unspecified,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = legendY,
            color = legendColor,
            modifier = Modifier.padding(start = 10.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
            Text(
                text = legendX,
                color = legendColor,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}

/**
 *
 */
@Composable
fun ChartCanvas(
    modifier: Modifier = Modifier,
    block: DrawScope.() -> Unit
) {
    Canvas(modifier = modifier) {
        scale(
            scaleX = 1f,
            scaleY = -1f,
            block = block
        )
    }
}

@Preview
@Composable
fun ChartPreview() {
    Chart(
        legendX = "X Axis",
        legendY = "Y Axis"
    ) {}
}