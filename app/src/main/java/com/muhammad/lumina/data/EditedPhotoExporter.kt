package com.muhammad.lumina.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.ui.unit.IntSize
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import com.muhammad.lumina.R
import com.muhammad.lumina.domain.model.Child
import com.muhammad.lumina.domain.model.ScaledChild
import com.muhammad.lumina.domain.repository.PhotoExporter
import com.muhammad.lumina.utils.ChildRenderCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class EditedPhotoExporter(
    private val context: Context,
) : PhotoExporter {
    private val childRenderCalculator =
        ChildRenderCalculator(context.resources.displayMetrics.density)

    override suspend fun exportEditedPhoto(
        backgroundImageBytes: ByteArray,
        children: List<Child>,
        editPhotoSize: IntSize,
        fileName: String,
    ): Result<String> = withContext(Dispatchers.IO) {
        var bitmap: Bitmap? = null
        var outputBitmap: Bitmap? = null
        try {
            bitmap =
                BitmapFactory.decodeByteArray(backgroundImageBytes, 0, backgroundImageBytes.size)
            outputBitmap = renderChildren(
                background = bitmap, children = children, editPhotoSize = editPhotoSize
            )
            val filePath = File(context.cacheDir, fileName).absolutePath
            val file = File(filePath)
            FileOutputStream(file).use { outputStream ->
                outputBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            Result.success(file.absolutePath)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            Result.failure(e)
        } finally {
            bitmap?.recycle()
            outputBitmap?.recycle()
        }
    }

    private suspend fun renderChildren(
        background: Bitmap,
        children: List<Child>,
        editPhotoSize: IntSize,
    ): Bitmap = withContext(Dispatchers.Default) {
        val output = background.copy(
            Bitmap.Config.ARGB_8888,
            true
        )
        val canvas = Canvas(output)
        val scaleFactors = childRenderCalculator.calculateScaleFactors(
            bitmapWidth = output.width, bitmapHeight = output.height, editPhotoSize = editPhotoSize
        )
        val scaledChildren = children.map { child ->
            childRenderCalculator.calculateScaledChild(
                child = child,
                scaleFactors = scaleFactors,
                editPhotoSize = editPhotoSize
            )
        }
        scaledChildren.forEach { scaledChild ->
            drawText(canvas, scaledChild)
        }
        drawWatermark(canvas = canvas, bitmapWidth = output.width, bitmapHeight = output.height)
        output
    }

    private fun drawWatermark(
        canvas: Canvas,
        bitmapWidth: Int,
        bitmapHeight: Int, text: String = "Edited by Lumina",
    ) {
        val typeface = ResourcesCompat.getFont(context, R.font.bricolage) ?: Typeface.DEFAULT_BOLD
        val textSize = bitmapWidth * 0.028f
        val paddingX = textSize * 0.9f
        val paddingY = textSize * 0.6f
        val margin = bitmapWidth * 0.04f

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface
            this.textSize = textSize
            color = Color.WHITE
            alpha = 210
        }
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            alpha = 120
        }
        val textWidth = textPaint.measureText(text)
        val fontMetrics = textPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top

        val boxWidth = textWidth + paddingX * 2
        val boxHeight = textHeight + paddingY * 2

        val left = bitmapWidth - boxWidth - margin
        val top = bitmapHeight- boxHeight - margin
        val right = left + boxWidth
        val bottom = top + boxHeight

        canvas.drawRoundRect(
            left, top, right, bottom, boxHeight / 2f, boxHeight / 2f, bgPaint
        )
        val textX = left + paddingX
        val textY = top + paddingY - fontMetrics.top
        canvas.drawText(text, textX, textY, textPaint)
    }

    private fun drawText(canvas: Canvas, scaledChild: ScaledChild) {
        val bricolageTypeface = ResourcesCompat.getFont(
            context, R.font.bricolage
        ) ?: Typeface.DEFAULT_BOLD
        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = scaledChild.strokeWidth
            textSize = scaledChild.scaledFontSizePx
            typeface = bricolageTypeface
            color = Color.BLACK
        }
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textSize = scaledChild.scaledFontSizePx
            typeface = bricolageTypeface
            color = Color.WHITE
        }
        val strokeLayout = StaticLayout.Builder.obtain(
            scaledChild.text,
            0,
            scaledChild.text.length,
            TextPaint(strokePaint),
            scaledChild.constraintWidth
        ).setAlignment(Layout.Alignment.ALIGN_CENTER).setIncludePad(false).build()
        val fillLayout = StaticLayout.Builder.obtain(
            scaledChild.text,
            0,
            scaledChild.text.length,
            TextPaint(fillPaint),
            scaledChild.constraintWidth
        ).setAlignment(Layout.Alignment.ALIGN_CENTER).setIncludePad(false).build()
        val textHeight = strokeLayout.height.toFloat()
        val textWidth =
            (0 until strokeLayout.lineCount).maxOfOrNull { strokeLayout.getLineWidth(it) } ?: 0f

        val boxWidth = textWidth + scaledChild.textPaddingX * 2
        val boxHeight = textHeight + scaledChild.textPaddingY * 2

        val centerX = scaledChild.scaledOffset.x + boxWidth / 2f
        val centerY = scaledChild.scaledOffset.y + boxHeight / 2f

        canvas.withTranslation(centerX, centerY) {
            scale(scaledChild.scale, scaledChild.scale)
            rotate(scaledChild.rotation)

            val textCenteringOffset = (scaledChild.constraintWidth - textWidth) / 2f

            translate(
                -boxWidth / 2f + scaledChild.textPaddingX - textCenteringOffset,
                -boxHeight / 2f + scaledChild.textPaddingY
            )
            strokeLayout.draw(this)
            fillLayout.draw(this)
        }
    }
}