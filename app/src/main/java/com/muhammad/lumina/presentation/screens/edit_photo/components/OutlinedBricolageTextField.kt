package com.muhammad.lumina.presentation.screens.edit_photo.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize

@Composable
fun OutlinedBricolageTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    strokeTextStyle: TextStyle,
    fillTextStyle: TextStyle,
    maxWidth: Dp? = null,
    maxHeight: Dp? = null,
) {
    val density = LocalDensity.current
    val measurer = rememberTextMeasurer()
    val constraints = calculateTextConstraints(maxWidth = maxWidth, maxHeight = maxHeight)
    val textLayoutResult = remember(text, strokeTextStyle, constraints) {
        measurer.measure(
            text = text, style = strokeTextStyle, constraints = constraints
        )
    }
    val textBoundingSize = with(density) {
        DpSize(
            width = textLayoutResult.size.width.toDp(),
            height = textLayoutResult.size.height.toDp()
        )
    }
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        cursorBrush = SolidColor(Color.White),
        singleLine = false, textStyle = fillTextStyle, decorationBox = { innerTextField ->
            Text(text = text, style = strokeTextStyle)
            innerTextField()
        }, modifier = modifier.size(textBoundingSize)
    )
}

@Composable
private fun calculateTextConstraints(maxWidth: Dp?, maxHeight: Dp?): Constraints {
    val density = LocalDensity.current
    return with(density) {
        Constraints(
            maxWidth = maxWidth?.roundToPx() ?: Int.MAX_VALUE,
            maxHeight = maxHeight?.roundToPx() ?: Int.MAX_VALUE
        )
    }
}