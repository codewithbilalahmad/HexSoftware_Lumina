package com.muhammad.lumina.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import com.muhammad.lumina.LuminaApplication

fun decodeBitmapFromPath(
    uri: String,
    maxSize: Int = 1080,
): Bitmap? {
    val context = LuminaApplication.INSTANCE
    return try {
        val parsedUri = uri.toUri()
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        context.contentResolver.openInputStream(parsedUri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, options)
        }
        var simpleSize = 1
        while (options.outWidth / simpleSize > maxSize || options.outHeight / simpleSize > maxSize) {
            simpleSize *= 2
        }
        val finalOptions = BitmapFactory.Options().apply {
            inSampleSize = simpleSize
            inPreferredConfig = Bitmap.Config.RGB_565
        }
        context.contentResolver.openInputStream(parsedUri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, finalOptions)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}