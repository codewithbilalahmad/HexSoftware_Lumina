package com.muhammad.lumina.domain.repository

import androidx.compose.ui.unit.IntSize
import com.muhammad.lumina.domain.model.Child
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface PhotoExporter {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun exportEditedPhoto(
        backgroundImageBytes : ByteArray,
        children : List<Child>,
        editPhotoSize : IntSize,
        fileName : String = "editedPhoto_${Uuid.random().toString()}.jpg"
    ) : Result<String>
}