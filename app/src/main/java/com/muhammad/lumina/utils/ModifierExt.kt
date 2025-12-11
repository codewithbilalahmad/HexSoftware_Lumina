package com.muhammad.lumina.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.dashedBorder(
    brush: Brush, shape: Shape, strokeWidth: Dp = 2.dp,
    dashedLength: Dp = 4.dp, gapLength: Dp = 4.dp, strokeCap: StrokeCap = StrokeCap.Round,
) = this.drawWithContent {
    val outlined =
        shape.createOutline(size = size, layoutDirection = layoutDirection, density = this)
    val dashedStroke = Stroke(
        width = strokeWidth.toPx(), cap = strokeCap, pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashedLength.toPx(), gapLength.toPx())
        )
    )
    drawContent()
    drawOutline(outline = outlined, style = dashedStroke, brush = brush)
}

@Composable
fun Modifier.rippleClickable(onClick: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this.clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
    )
}