package com.muhammad.lumina.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.muhammad.lumina.presentation.theme.bricolage

@Composable
fun rememberFillTextStyle(
    fontSize : TextUnit,
    fontFamily : FontFamily = bricolage,
    fillColor : Color = Color.White,
    textAlign: TextAlign = TextAlign.Center,
) : TextStyle{
    return remember(fontSize, fontFamily, fillColor, textAlign) {
        TextStyle(
            color = fillColor,
            textAlign = textAlign,
            fontSize = fontSize,
            fontFamily = fontFamily
        )
    }
}