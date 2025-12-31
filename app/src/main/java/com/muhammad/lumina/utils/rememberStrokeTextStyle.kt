package com.muhammad.lumina.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.muhammad.lumina.presentation.theme.bricolage

@Composable
fun rememberStrokeTextStyle(
    fontSize: TextUnit,
    fontFamily: FontFamily = bricolage,
    strokeWidth: Dp = 3.dp,
    strokeColor: Color = Color.Black,
    textAlign: TextAlign = TextAlign.Center,
): TextStyle {
    val density = LocalDensity.current
    return remember(fontSize, fontFamily, strokeWidth, strokeColor, textAlign) {
        TextStyle(
            color = strokeColor,
            fontSize = fontSize,
            fontFamily = fontFamily,
            textAlign = textAlign,
            drawStyle = Stroke(
                width = with(density) { strokeWidth.toPx() },
                cap = StrokeCap.Round, join = StrokeJoin.Round, miter = 10f
            )
        )
    }
}