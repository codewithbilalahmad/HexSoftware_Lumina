package com.muhammad.lumina.presentation.screens.edit_photo

import android.graphics.Bitmap
import androidx.compose.ui.unit.IntSize
import com.muhammad.lumina.domain.model.EditPhotoFeature
import com.muhammad.lumina.domain.model.Emoji
import com.muhammad.lumina.domain.model.Child
import com.muhammad.lumina.domain.model.EmojiType
import com.muhammad.lumina.domain.model.PhotoFilter
import com.muhammad.lumina.utils.ACTIVITY_AND_SPORTS
import com.muhammad.lumina.utils.ANIMALS_AND_NATURE
import com.muhammad.lumina.utils.FOOD_AND_DRINKS
import com.muhammad.lumina.utils.OBJECTS
import com.muhammad.lumina.utils.SMILEYS_AND_PEOPLE
import com.muhammad.lumina.utils.SYMBOLS
import com.muhammad.lumina.utils.TRAVEL_AND_PLACES

data class EditPhotoState(
    val originalBitmap: Bitmap? = null,
    val editedBitmap: Bitmap? = null,
    val selectedPhotoFilter: PhotoFilter = PhotoFilter.NORMAL,
    val showSaveImageToGalleryDialog: Boolean = false,
    val showExitEditingDialog: Boolean = false,
    val selectedFeature: EditPhotoFeature? = null,
    val isSavingImageToGallery: Boolean = false,
    val brightness: Float = 0f,
    val contrast: Float = 1f,
    val saturation: Float = 1f,
    val rotation: Float = 0f,
    val flipHorizontal: Boolean = false,
    val flipVertical: Boolean = false,
    val showEmojiPickerBottomSheet: Boolean = false,
    val emojiMap: Map<EmojiType, List<Emoji>> = mapOf(
        EmojiType.SMILEYS_AND_PEOPLE to SMILEYS_AND_PEOPLE,
        EmojiType.ANIMALS_AND_NATURE to ANIMALS_AND_NATURE,
        EmojiType.FOOD_AND_DRINKS to FOOD_AND_DRINKS,
        EmojiType.ACTIVITY_AND_SPORTS to ACTIVITY_AND_SPORTS,
        EmojiType.TRAVEL_AND_PLACES to TRAVEL_AND_PLACES,
        EmojiType.OBJECTS to OBJECTS,
        EmojiType.SYMBOLS to SYMBOLS
    ),
    val showEditMenuDropdown : Boolean = false,
    val childInteractionState: ChildInteractionState = ChildInteractionState.None,
    val children: List<Child> = emptyList(),
    val editPhotoSize: IntSize = IntSize.Zero,
)
