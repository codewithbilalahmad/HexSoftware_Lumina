package com.muhammad.lumina.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TransparentGridBackground(
    modifier: Modifier = Modifier,
    cellSize: Dp = 20.dp,
    lightColor: Color = Color(0xFFE0E0E0),
    darkColor: Color = Color(0xFFBDBDBD),
) {
    Canvas(modifier = modifier) {
        val cellPx = cellSize.toPx()
        val columns = (size.width / cellPx).toInt() + 1
        val rows = (size.height / cellPx).toInt() + 1
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val isDark = (row + col) % 2 == 0
                val color = if (isDark) darkColor else lightColor
                drawRect(
                    color = color,
                    topLeft = Offset(x = col * cellPx, y = row * cellPx),
                    size = Size(cellPx, cellPx)
                )
            }
        }
    }
}