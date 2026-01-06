package com.muhammad.lumina.utils

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.muhammad.lumina.domain.model.Celebration
import com.muhammad.lumina.domain.model.CelebrationType
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class CelebrationSystem(private val onFinished: () -> Unit) {
    private val celebrations = mutableListOf<Celebration>()
    private val random = Random
    private val maxCelebrations = 100
    private var hasSpawned = false
    private var finishedCalled = false
    fun start(width: Float) {
        if (hasSpawned) return
        hasSpawned = true
        repeat(maxCelebrations) {
            spawnCelebration(width)
        }
    }

    fun update(width: Float, height: Float, deltaTime: Float) {
        val iterator = celebrations.iterator()
        while (iterator.hasNext()) {
            val celebration = iterator.next()
            celebration.vx = sin(celebration.y * 0.01f + celebration.x * 0.005f) * 30f
            celebration.x += celebration.vx * deltaTime
            celebration.y += celebration.vy * deltaTime
            celebration.rotation =
                (celebration.rotation + celebration.rotationSpeed * deltaTime) % 360f
            celebration.alpha = 1f - (celebration.y / height).coerceIn(0f, 1f)
            if (celebration.y > height + 20f || celebration.x < -20f || celebration.x > width + 20f) {
                iterator.remove()
            }
            if (hasSpawned && celebrations.isEmpty() && !finishedCalled) {
                finishedCalled = true
                onFinished()
            }
        }
    }

    private fun spawnCelebration(width: Float) {
        val type = when {
            random.nextFloat() < 0.1f -> CelebrationType.CIRCLE
            random.nextFloat() < 0.25f -> CelebrationType.ROUNDED_RECT
            random.nextFloat() < 0.45f -> CelebrationType.CONFETTI_LINE
            random.nextFloat() < 0.55f -> CelebrationType.SNOW
            random.nextFloat() < 0.75f -> CelebrationType.HEXAGON
            else -> CelebrationType.RECT
        }
        val celebrationColors = listOf(
            Color(0xFFFFC107), // Amber
            Color(0xFF03DAC5), // Teal
            Color(0xFFFF5722), // Deep Orange
            Color(0xFF2196F3), // Blue
            Color(0xFFE91E63), // Pink
            Color(0xFF4CAF50), // Green
            Color(0xFF9C27B0)  // Purple
        )
        val size = random.nextFloat() * 15f + 20f
        celebrations.add(
            Celebration(
                x = random.nextFloat() * width,
                y = -30f,
                vx = 0f, vy = random.nextFloat() * 180f + 100f,
                size = size,
                alpha = 1f,
                rotation = random.nextFloat() * 360f,
                color = celebrationColors.random(),
                rotationSpeed = random.nextFloat() * 180f,
                type = type
            )
        )
    }

    fun draw(drawScope: DrawScope) {
        for (celebration in celebrations) {
            with(drawScope.drawContext.canvas) {
                save()
                translate(celebration.x, celebration.y)
                rotate(celebration.rotation)
                val color = celebration.color.copy(alpha = celebration.alpha)
                when (celebration.type) {
                    CelebrationType.CIRCLE -> {
                        drawScope.drawCircle(
                            radius = celebration.size, center = Offset.Zero,
                            color = color,
                        )
                    }

                    CelebrationType.RECT -> {
                        drawScope.drawRect(
                            color = color,
                            size = Size(celebration.size, celebration.size),
                            topLeft = Offset(-celebration.size / 2f, -celebration.size / 2f)
                        )
                    }

                    CelebrationType.ROUNDED_RECT -> {
                        drawScope.drawRoundRect(
                            color = color,
                            size = Size(celebration.size, celebration.size),
                            cornerRadius = CornerRadius(
                                celebration.size / 4f,
                                celebration.size / 4f,
                            ),
                            topLeft = Offset(-celebration.size / 2f, -celebration.size / 2f)
                        )
                    }

                    CelebrationType.CONFETTI_LINE -> {
                        drawScope.drawRect(
                            color = color,
                            size = Size(
                                width = celebration.size * 0.35f,
                                height = celebration.size * 1.2f
                            ), topLeft = Offset(
                                x = -celebration.size * 0.18f,
                                y = -celebration.size * 0.7f
                            )
                        )
                    }

                    CelebrationType.SNOW -> {
                        val arms = 12
                        val armLength = celebration.size
                        for (i in 0 until arms) {
                            val angle = (i * 30 + celebration.rotation) * PI.toFloat() / 180f
                            val endX = cos(angle) * armLength
                            val endY = sin(angle) * armLength
                            drawScope.drawLine(
                                color = color, start = Offset.Zero, end = Offset(endX, endY),
                                strokeWidth = 2f, cap = StrokeCap.Round
                            )
                        }
                    }

                    CelebrationType.HEXAGON ->{
                        drawScope.drawHexagon(size = celebration.size , color = color)
                    }
                }
                restore()
            }
        }
    }
    private fun DrawScope.drawHexagon(size : Float,color : Color){
        val path = Path()
        repeat(6){i ->
            val angle = (PI / 3 * i - PI / 2).toFloat()
            val x = cos(angle) * size
            val y = sin(angle) * size
            if(i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawPath(path = path, color = color)
    }
}