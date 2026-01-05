package com.muhammad.lumina.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.muhammad.lumina.utils.CelebrationSystem

@Composable
fun CelebrationOverlay(modifier: Modifier = Modifier, isVisible : Boolean,onFinished : () -> Unit) {
    if(!isVisible) return
    BoxWithConstraints(modifier = modifier.fillMaxSize()){
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()
        val celebrationSystem = remember { CelebrationSystem(onFinished = onFinished) }
        var lastFrameTime by remember { mutableLongStateOf(0L) }
        LaunchedEffect(isVisible) {
            if(isVisible){
                lastFrameTime = 0L
                celebrationSystem.start(width)
            }
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val now = System.nanoTime()
            val deltaTime = if(lastFrameTime == 0L){
                0f
            } else{
                (now - lastFrameTime) / 1_000_000_000f
            }
            lastFrameTime = now
            celebrationSystem.update(
                width = width, height = height, deltaTime = deltaTime
            )
            celebrationSystem.draw(this)
        }
    }
}