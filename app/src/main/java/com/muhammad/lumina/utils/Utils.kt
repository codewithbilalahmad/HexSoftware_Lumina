package com.muhammad.lumina.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.util.TypedValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


fun createBeautifulGradient(): List<Color> {
    val colorPool = listOf(
        Color(0xFF81C784), // Soft Green
        Color(0xFF64B5F6), // Soft Blue
        Color(0xFF4DD0E1), // Soft Cyan
        Color(0xFFFF8A65), // Soft Orange
        Color(0xFFBA68C8), // Soft Purple
        Color(0xFF4DB6AC), // Soft Teal
        Color(0xFF7986CB), // Soft Indigo
        Color(0xFFF06292), // Soft Pink
        Color(0xFFE57373), // Soft Red
    )
    return colorPool.shuffled().take(3)
}

fun TextUnit.toPx() : Float{
    val metrics = Resources.getSystem().displayMetrics
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.value,
        metrics
    )
}

suspend fun imageBitmapToByteArray(bitmap : Bitmap) : ByteArray{
    return withContext(Dispatchers.IO){
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,outputStream )
        outputStream.toByteArray()
    }
}