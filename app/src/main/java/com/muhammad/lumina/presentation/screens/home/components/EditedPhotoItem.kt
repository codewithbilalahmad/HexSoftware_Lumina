package com.muhammad.lumina.presentation.screens.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.muhammad.lumina.domain.model.EditedPhoto
import com.muhammad.lumina.utils.decodeBitmapFromPath

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EditedPhotoItem(
    modifier: Modifier = Modifier, editedPhoto: EditedPhoto,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope, onClick: () -> Unit,
) {
    val bitmap = remember(editedPhoto.uri) {
        decodeBitmapFromPath(editedPhoto.uri)
    }
    bitmap?.let { bit ->
        with(sharedTransitionScope) {
            Image(
                bitmap = bit.asImageBitmap(),
                contentDescription = null,
                modifier = modifier
                    .clip(MaterialShapes.Square.toShape())
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(colors = editedPhoto.colors),
                        shape = MaterialShapes.Square.toShape()
                    )
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            key = "editedImage_${editedPhoto.uri}"
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 300, easing = FastOutLinearInEasing)
                        })
                    .clickable {
                        onClick()
                    },
                contentScale = ContentScale.Crop
            )
        }
    }
}