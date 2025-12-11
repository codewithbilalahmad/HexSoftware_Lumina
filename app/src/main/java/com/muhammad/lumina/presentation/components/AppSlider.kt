package com.muhammad.lumina.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    thumbSize: Dp = 30.dp,
    trackHeight: Dp = 8.dp,
) {
    val minValue = valueRange.start
    val maxValue = valueRange.endInclusive
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    var trackWidthPx by remember { mutableFloatStateOf(0f) }
    val thumbSizePx = with(density) { thumbSize.toPx() }
    val halfThumbPx = thumbSizePx / 2f
    val offsetX = remember { Animatable(0f) }
    LaunchedEffect(value, trackWidthPx) {
        if (trackWidthPx <= 0f) return@LaunchedEffect
        val safeTrack = max(1f, trackWidthPx)
        val position = ((value - minValue) / (maxValue - minValue)).coerceIn(0f, 1f) * safeTrack
        offsetX.animateTo(targetValue = position, animationSpec = tween(durationMillis = 150))
    }
    Box(
        modifier = modifier
            .height(thumbSize)
            .padding(horizontal = 12.dp)
            .onGloballyPositioned { coordinates ->
                val width = coordinates.size.width.toFloat()
                if (width != trackWidthPx) {
                    trackWidthPx = width
                    scope.launch {
                        val safeTrack = max(1f, trackWidthPx)
                        val position =
                            ((value - minValue) / (maxValue - minValue)).coerceIn(0f, 1f) * safeTrack
                        offsetX.snapTo(position)
                    }
                }
            }
            .pointerInput(trackWidthPx) {
                detectTapGestures { offset ->
                    val safeTrack = max(1f, trackWidthPx)
                    val x = offset.x.coerceIn(0f, safeTrack)
                    val newValue = (x / safeTrack).let { value ->
                        minValue + value * (maxValue - minValue)
                    }
                    onValueChange(newValue)
                    scope.launch {
                        offsetX.snapTo(x)
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Box(
            modifier = Modifier
                .width(with(density) {
                    (offsetX.value).toDp()
                })
                .height(trackHeight)
                .align(Alignment.CenterStart)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        Box(
            modifier = Modifier
                .offset {
                    val x = (offsetX.value - halfThumbPx).roundToInt()
                    IntOffset(x = x, y = 0)
                }
                .size(thumbSize)
                .clip(MaterialShapes.Cookie12Sided.toShape())
                .background(MaterialTheme.colorScheme.primary)
                .pointerInput(trackWidthPx) {
                    detectDragGestures{change, dragAmount ->
                        change.consume()
                        val safeTrack = max(1f, trackWidthPx)
                        val newX = (offsetX.value + dragAmount.x).coerceIn(0f, safeTrack)
                        scope.launch {
                            offsetX.snapTo(newX)
                        }
                        val newValue = (newX / safeTrack).let { value ->
                            minValue + value * (maxValue - minValue)
                        }
                        onValueChange(newValue)
                    }
                })
    }
}
