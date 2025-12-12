package com.muhammad.lumina.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.muhammad.lumina.domain.model.PhotoFilter

@SuppressLint("UseKtx")
fun applyPhotoFilter(original: Bitmap, filter: PhotoFilter): Bitmap {
    val width = original.width
    val height = original.height
    val filteredBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(filteredBitmap)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    when (filter) {
        PhotoFilter.NORMAL -> Unit
        PhotoFilter.SEPIA -> {
            colorMatrix.set(
                floatArrayOf(
                    0.393f, 0.769f, 0.189f, 0f, 0f,
                    0.349f, 0.686f, 0.168f, 0f, 0f,
                    0.272f, 0.534f, 0.131f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.WARM -> {
            colorMatrix.set(
                floatArrayOf(
                    1.12f, 0f, 0f, 0f, 10f,
                    0f, 1.04f, 0f, 0f, 6f,
                    0f, 0f, 0.95f, 0f, -6f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.COOL -> {
            colorMatrix.set(
                floatArrayOf(
                    0.95f, 0f, 0f, 0f, -6f,
                    0f, 0.98f, 0f, 0f, 0f,
                    0f, 0f, 1.12f, 0f, 10f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.TEAL_ORANGE -> {
            colorMatrix.set(
                floatArrayOf(
                    1.15f, -0.05f, -0.05f, 0f, 6f,
                    -0.05f, 1.05f, -0.05f, 0f, 6f,
                    -0.05f, -0.05f, 1.15f, 0f, 10f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.VINTAGE -> {
            colorMatrix.set(
                floatArrayOf(
                    0.9f, 0f, 0f, 0f, 12f,
                    0f, 0.85f, 0f, 0f, 8f,
                    0f, 0f, 0.8f, 0f, -6f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.MOODY -> {
            colorMatrix.set(
                floatArrayOf(
                    0.8f, 0.02f, 0f, 0f, -20f,
                    0f, 0.9f, 0.02f, 0f, -12f,
                    0f, 0f, 0.95f, 0f, -6f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.BLACK_WHITE -> {
            colorMatrix.set(
                floatArrayOf(
                    0.33f, 0.59f, 0.11f, 0f, 0f,
                    0.33f, 0.59f, 0.11f, 0f, 0f,
                    0.33f, 0.59f, 0.11f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.CINEMATIC -> {
            colorMatrix.set(
                floatArrayOf(
                    1.2f, -0.1f, -0.1f, 0f, 10f,
                    -0.05f, 1.1f, -0.1f, 0f, 5f,
                    -0.1f, -0.05f, 1.3f, 0f, 15f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.FADE_FILM -> {
            colorMatrix.set(
                floatArrayOf(
                    0.9f, 0f, 0f, 0f, 12f,
                    0f, 0.9f, 0f, 0f, 12f,
                    0f, 0f, 0.9f, 0f, 12f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.MATTE -> {
            colorMatrix.set(
                floatArrayOf(
                    0.85f, 0f, 0f, 0f, 20f,
                    0f, 0.85f, 0f, 0f, 20f,
                    0f, 0f, 0.85f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.VIBRANT -> {
            colorMatrix.set(
                floatArrayOf(
                    1.15f, 0.05f, 0.05f, 0f, 0f,
                    0.05f, 1.15f, 0.05f, 0f, 0f,
                    0.05f, 0.05f, 1.15f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.SOFT_PASTEL -> {
            colorMatrix.set(
                floatArrayOf(
                    1.05f, -0.05f, -0.05f, 0f, 10f,
                    -0.05f, 1.05f, -0.05f, 0f, 10f,
                    -0.05f, -0.05f, 1.05f, 0f, 15f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.LUSH_GREEN -> {
            colorMatrix.set(
                floatArrayOf(
                    1.0f, -0.05f, -0.05f, 0f, 0f,
                    0f, 1.25f, 0f, 0f, 5f,
                    0f, -0.05f, 1.05f, 0f, 5f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.SUNNY_GLOW -> {
            colorMatrix.set(
                floatArrayOf(
                    1.15f, 0.05f, 0f, 0f, 10f,
                    0.05f, 1.10f, 0f, 0f, 8f,
                    0f, 0f, 0.95f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.MONO -> {
            colorMatrix.set(
                floatArrayOf(
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0.33f, 0.33f, 0.33f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.SOFT_MONO -> {
            colorMatrix.set(
                floatArrayOf(
                    0.28f, 0.28f, 0.28f, 0f, 20f,
                    0.28f, 0.28f, 0.28f, 0f, 20f,
                    0.28f, 0.28f, 0.28f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.HIGH_CONTRAST_BW -> {
            colorMatrix.set(
                floatArrayOf(
                    1.5f * 0.33f, 1.5f * 0.33f, 1.5f * 0.33f, 0f, -20f,
                    1.5f * 0.33f, 1.5f * 0.33f, 1.5f * 0.33f, 0f, -20f,
                    1.5f * 0.33f, 1.5f * 0.33f, 1.5f * 0.33f, 0f, -20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.FILM_GRAIN_BW -> {
            colorMatrix.set(
                floatArrayOf(
                    0.32f, 0.32f, 0.32f, 0f, 12f,
                    0.32f, 0.32f, 0.32f, 0f, 12f,
                    0.32f, 0.32f, 0.32f, 0f, 12f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.RETRO -> {
            colorMatrix.set(
                floatArrayOf(
                    1.2f, -0.2f, 0f, 0f, 20f,
                    0f, 1.1f, -0.1f, 0f, 10f,
                    -0.1f, 0.1f, 1.1f, 0f, 5f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.FILTER_1977 -> {
            colorMatrix.set(
                floatArrayOf(
                    1.2f, 0f, 0.1f, 0f, 20f,
                    0f, 1.1f, 0.1f, 0f, 10f,
                    0f, 0f, 1.4f, 0f, 15f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.OLD_PAPER -> {
            colorMatrix.set(
                floatArrayOf(
                    1f, 0.1f, 0f, 0f, 25f,
                    0.1f, 1f, 0f, 0f, 20f,
                    0f, 0f, 0.8f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.WASHED -> {
            colorMatrix.set(
                floatArrayOf(
                    0.8f, 0.1f, 0.1f, 0f, 20f,
                    0.1f, 0.8f, 0.1f, 0f, 20f,
                    0.1f, 0.1f, 0.8f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.DUOTONE_BLUE_RED -> {
            colorMatrix.set(
                floatArrayOf(
                    0.2f, 0.1f, 0.9f, 0f, 0f,
                    0.1f, 0.1f, 0.1f, 0f, 0f,
                    0.9f, 0.1f, 0.2f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.DUOTONE_PURPLE_TEAL -> {
            colorMatrix.set(
                floatArrayOf(
                    0.5f, 0.2f, 0.8f, 0f, 0f,
                    0.1f, 0.5f, 0.6f, 0f, 0f,
                    0.8f, 0.3f, 0.5f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.DARK_MOODY -> {
            colorMatrix.set(
                floatArrayOf(
                    1.1f, -0.1f, 0f, 0f, -15f,
                    -0.05f, 1.0f, -0.1f, 0f, -10f,
                    0f, -0.1f, 1.2f, 0f, -5f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.DRAMATIC -> {
            colorMatrix.set(
                floatArrayOf(
                    1.4f, -0.1f, -0.1f, 0f, -20f,
                    -0.1f, 1.4f, -0.1f, 0f, -20f,
                    -0.1f, -0.1f, 1.4f, 0f, -20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.SOFT_DREAM -> {
            colorMatrix.set(
                floatArrayOf(
                     1.1f, -0.1f, -0.1f, 0f, 20f,
                    -0.1f, 1.1f, -0.1f, 0f, 20f,
                    -0.1f, -0.1f, 1.1f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.HAZE -> {
            colorMatrix.set(
                floatArrayOf(
                    0.9f, 0.05f, 0.05f, 0f, 30f,
                    0.05f, 0.9f, 0.05f, 0f, 30f,
                    0.05f, 0.05f, 0.9f, 0f, 30f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.VIGNETTE -> {
            colorMatrix.set(
                floatArrayOf(
                    1.05f, -0.05f, -0.05f, 0f, -10f,
                    -0.05f, 1.05f, -0.05f, 0f, -10f,
                    -0.05f, -0.05f, 1.05f, 0f, -10f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.BLOSSOM -> {
            colorMatrix.set(
                floatArrayOf(
                    1.1f, 0.1f, 0.2f, 0f, 15f,
                    0.05f, 1.05f, 0.05f, 0f, 10f,
                    0.1f, 0.05f, 0.9f, 0f, 5f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.NIGHT_BLUE -> {
            colorMatrix.set(
                floatArrayOf(
                    0.8f, 0f,   0f,   0f, -10f,
                    0f,   0.9f, 0f,   0f, -5f,
                    0.2f, 0.2f, 1.3f, 0f, 15f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.CYBERPUNK -> {
            colorMatrix.set(
                floatArrayOf(
                    1.3f, -0.2f, 0.6f, 0f, 10f,
                    0f,   0.9f,  0.3f, 0f, 0f,
                    0.5f, 0.1f,  1.4f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.NEON_POP -> {
            colorMatrix.set(
                floatArrayOf(
                    1.4f, 0.1f, 0.1f, 0f, 10f,
                    0.1f, 1.4f, 0.1f, 0f, 10f,
                    0.1f, 0.1f, 1.4f, 0f, 10f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }

        PhotoFilter.GOLDEN_HOUR -> {
            colorMatrix.set(
                floatArrayOf(
                    1.2f, 0.1f, 0f,   0f, 20f,
                    0.05f, 1.1f, 0f,  0f, 10f,
                    0f,   0.05f, 0.9f,0f, -5f,
                    0f,   0f,    0f,  1f, 0f
                )
            )
        }
        PhotoFilter.CINNAMON -> {
            colorMatrix.set(
                floatArrayOf(
                    1.15f, 0.05f, 0f, 0f, 10f,
                    0.05f, 1.05f, 0f, 0f, 5f,
                    0f,    0.05f, 0.85f,0f, -10f,
                    0f,    0f,    0f,   1f, 0f
                )
            )
        }
        PhotoFilter.ROSE_GOLD -> {
            colorMatrix.set(
                floatArrayOf(
                    1.1f, 0.05f, 0.1f, 0f, 15f,
                    0.05f,1f,    0.15f,0f, 10f,
                    0.1f, 0.05f, 0.9f, 0f, -5f,
                    0f,   0f,    0f,   1f, 0f
                )
            )
        }
        PhotoFilter.CARAMEL -> {
            colorMatrix.set(
                floatArrayOf(
                    1.1f, 0.1f, 0f,   0f, 15f,
                    0.05f,1.05f,0f,   0f, 10f,
                    0f,   0.05f,0.8f, 0f, -10f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
        }
        PhotoFilter.AMBER_GLOW -> {
            colorMatrix.set(
                floatArrayOf(
                    1.25f,0.1f,  0f,   0f, 25f,
                    0.1f, 1.1f,  0f,   0f, 15f,
                    0f,   0.05f, 0.85f,0f, -5f,
                    0f,   0f,    0f,   1f, 0f
                )
            )
        }
        PhotoFilter.FROST -> {
            colorMatrix.set(
                floatArrayOf(
                    0.9f, 0.05f, 0.1f,  0f, 5f,
                    0.05f,0.95f, 0.15f, 0f, 5f,
                    0.1f, 0.1f,  1.2f,  0f, 10f,
                    0f,   0f,    0f,    1f, 0f
                )
            )
        }
        PhotoFilter.GLACIER -> {
            colorMatrix.set(
                floatArrayOf(
                    0.85f,0.05f,0.2f, 0f, -10f,
                    0.05f,0.9f, 0.2f, 0f, -10f,
                    0.2f, 0.3f, 1.3f, 0f, 20f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
        }
        PhotoFilter.ARCTIC -> {
            colorMatrix.set(
                floatArrayOf(
                    0.8f, 0.1f, 0.1f,  0f, 0f,
                    0.1f, 0.85f,0.2f, 0f, 0f,
                    0.1f, 0.2f, 1.2f,  0f, 10f,
                    0f,   0f,   0f,    1f, 0f
                )
            )
        }
        PhotoFilter.AURORA -> {
            colorMatrix.set(
                floatArrayOf(
                    0.9f, 0.15f,0f, 0f, 0f,
                    0.1f, 1f,   0.2f,0f, 10f,
                    0.2f, 0.3f, 1.1f,0f, 15f,
                    0f,   0f,   0f,  1f, 0f
                )
            )
        }
        PhotoFilter.OCEAN_DEEP -> {
            colorMatrix.set(
                floatArrayOf(
                    0.7f, 0.05f,0.2f, 0f, -20f,
                    0.05f,0.75f,0.25f,0f, -10f,
                    0.2f, 0.3f, 1.3f, 0f, 20f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
        }
        PhotoFilter.AQUA_MINT -> {
            colorMatrix.set(
                floatArrayOf(
                    0.9f, 0.1f,  0.1f, 0f, 5f,
                    0.1f, 1.1f,  0.2f, 0f, 10f,
                    0.1f, 0.2f,  1.05f,0f, 5f,
                    0f,   0f,    0f,   1f, 0f
                )
            )
        }
        PhotoFilter.EMERALD -> {
            colorMatrix.set(
                floatArrayOf(
                    0.9f, 0.2f, 0.1f, 0f, 0f,
                    0.15f,1.2f, 0.15f,0f, 20f,
                    0.05f,0.15f,0.9f,0f, -10f,
                    0f,    0f,   0f,   1f, 0f
                )
            )
        }
        PhotoFilter.FOREST_FADE -> {
            colorMatrix.set(
                floatArrayOf(
                    0.85f,0.1f, 0.05f,0f, -10f,
                    0.1f, 1.05f,0.1f, 0f, 15f,
                    0.05f,0.15f,0.9f, 0f, -5f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
        }
        PhotoFilter.DESERT_SAND -> {
            colorMatrix.set(
                floatArrayOf(
                    1.1f, 0.15f, 0.05f, 0f, 10f,
                    0.1f, 1.05f, 0.05f, 0f, 5f,
                    0.05f,0.05f, 0.9f,  0f, -5f,
                    0f,   0f,    0f,    1f, 0f
                )
            )
        }
        PhotoFilter.SUNSET -> {
            colorMatrix.set(
                floatArrayOf(
                    1.3f, 0.1f, 0.1f, 0f, 20f,
                    0.1f, 1.1f, 0.1f, 0f, 10f,
                    0.05f,0.1f, 0.9f, 0f, -10f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
        }
        PhotoFilter.DREAMY_PINK -> {
            colorMatrix.set(
                floatArrayOf(
                    1.1f, 0.1f, 0.2f, 0f, 20f,
                    0.1f, 1f,   0.2f, 0f, 10f,
                    0.1f, 0.1f, 0.9f, 0f, -5f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
        }
        PhotoFilter.BLOOM -> {
            colorMatrix.set(
                floatArrayOf(
                    1.2f, 0.1f,  0.1f, 0f, 20f,
                    0.1f, 1.1f,  0.1f, 0f, 20f,
                    0.1f, 0.1f,  1.05f,0f, 10f,
                    0f,   0f,    0f,   1f, 0f
                )
            )
        }
        PhotoFilter.VELVET -> {
            colorMatrix.set(
                floatArrayOf(
                    1.05f,0.1f, 0.2f, 0f, 0f,
                    0.1f, 0.9f, 0.15f,0f, -5f,
                    0.1f, 0.15f,0.85f,0f, -10f,
                    0f,   0f,   0f,    1f, 0f
                )
            )
        }
        PhotoFilter.NOIR_FADE -> {
            colorMatrix.set(
                floatArrayOf(
                    0.55f,0.55f,0.55f,0f, 10f,
                    0.45f,0.45f,0.45f,0f, 5f,
                    0.35f,0.35f,0.35f,0f, -5f,
                    0f,   0f,   0f,   1f, 0f
                )
            )
        }
    }
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(original, 0f, 0f, paint)
    return filteredBitmap
}