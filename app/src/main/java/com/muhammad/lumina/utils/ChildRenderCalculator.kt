package com.muhammad.lumina.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.muhammad.lumina.domain.model.Child
import com.muhammad.lumina.domain.model.ScaleFactors
import com.muhammad.lumina.domain.model.ScaledChild
import kotlin.math.roundToInt

class ChildRenderCalculator(
    private val density: Float,
) {
    companion object {
        private const val TEXT_PADDING_DP = 8f
        private const val STROKE_WIDTH_DP = 3f
    }

    fun calculateScaleFactors(
        bitmapWidth: Int,
        bitmapHeight: Int,
        editPhotoSize: IntSize,
    ): ScaleFactors {
        val scaleX =
            if (editPhotoSize.width > 0) bitmapWidth.toFloat() / editPhotoSize.width else 1f
        val scaleY =
            if (editPhotoSize.height > 0) bitmapHeight.toFloat() / editPhotoSize.height else 1f
        val bitmapScale = (scaleX + scaleY) / 2
        return ScaleFactors(
            scaleX = scaleX, scaleY = scaleY, bitmapScale = bitmapScale
        )
    }

    fun calculateScaledChild(
        child: Child,
        scaleFactors: ScaleFactors,
        editPhotoSize: IntSize,
    ): ScaledChild {
        val (scaleX, scaleY, bitmapScale) = scaleFactors
        val scaledOffset = Offset(
            x = (child.offsetRatioX * editPhotoSize.width) * scaleX,
            y = (child.offsetRatioY * editPhotoSize.height) * scaleY
        )
        val textPaddingPx = TEXT_PADDING_DP * density
        val textPaddingX = textPaddingPx * scaleX
        val textPaddingY = textPaddingPx * scaleY
        val scaledFontSize = child.fontSize * bitmapScale
        val strokeWidth = STROKE_WIDTH_DP * density * scaleX

        val paddingDp = TEXT_PADDING_DP * 2
        val paddingPx = paddingDp * density

        val constraintWidth = ((editPhotoSize.width / child.scale) * scaleX - paddingPx * scaleX).roundToInt()
                .coerceAtLeast(0)
        return ScaledChild(
            text = child.text,
            scaledOffset = scaledOffset,
            textPaddingX = textPaddingX,
            textPaddingY = textPaddingY,
            originalChild = child,
            scale = child.scale, strokeWidth = strokeWidth, scaledFontSizePx = scaledFontSize.toPx(),
            rotation = child.rotation, constraintWidth = constraintWidth
        )
    }
}