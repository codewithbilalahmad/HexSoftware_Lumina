package com.muhammad.lumina.presentation.screens.edit_photo.components

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.muhammad.lumina.domain.model.EmojiLayer

@Composable
fun EmojiLayerItem(layer: EmojiLayer, onUpdate : (EmojiLayer) -> Unit) {
    var offset by remember { mutableStateOf(layer.offset) }
    var scale by remember { mutableFloatStateOf(layer.scale) }
    var rotation by remember { mutableFloatStateOf(layer.scale) }
    Text(text = layer.emoji, fontSize = 40.sp, modifier = Modifier.offset{
        IntOffset(x = offset.x.toInt(), y = offset.y.toInt())
    }.graphicsLayer{
        scaleX = scale
        scaleY = scale
        rotationZ = rotation
    }.pointerInput(Unit){
        detectTransformGestures { _, pan, zoom, rotate ->
            offset += pan
            scale *= zoom
            rotation += rotate
            onUpdate(
                layer.copy(
                    offset = offset, scale = scale, rotation = rotation
                )
            )
        }
    })
}