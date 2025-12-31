package com.muhammad.lumina.presentation.screens.edit_photo.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.muhammad.lumina.utils.rememberFillTextStyle
import com.muhammad.lumina.utils.rememberStrokeTextStyle

@Composable
fun OutlinedBricolageText(
    modifier: Modifier = Modifier,
    text: String,
    strokeTextStyle: TextStyle,
    fillTextStyle: TextStyle,
) {
    Box(modifier = modifier) {
        Text(text = text, style = strokeTextStyle)
        Text(text = text, style = fillTextStyle)
    }
}

@Preview
@Composable
private fun OutlinedBricolageTextPreview() {
    OutlinedBricolageText(
        text = "Tap to Edit!",
        strokeTextStyle = rememberStrokeTextStyle(36.sp),
        fillTextStyle = rememberFillTextStyle(36.sp)
    )
}