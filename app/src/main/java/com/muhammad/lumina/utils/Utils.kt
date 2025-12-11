package com.muhammad.lumina.utils

import androidx.compose.ui.graphics.Color


fun createBeautifulGradient(): List<Color> {
    val colorPool = listOf(
        Color(0xFF81C784), // Soft Green
        Color(0xFF64B5F6), // Soft Blue
        Color(0xFF4DD0E1), // Soft Cyan
        Color(0xFFFF8A65), // Soft Orange
        Color(0xFFBA68C8), // Soft Purple
        Color(0xFF4DB6AC), // Soft Teal
        Color(0xFF7986CB), // Soft Indigo
        Color(0xFFF06292), // Soft Pink
        Color(0xFFE57373), // Soft Red
    )
    return colorPool.shuffled().take(3)
}