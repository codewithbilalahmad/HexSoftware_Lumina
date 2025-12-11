package com.muhammad.lumina.presentation.screens.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.lumina.R
import com.muhammad.lumina.domain.model.EditedPhoto
import com.muhammad.lumina.utils.decodeBitmapFromPath
import com.muhammad.lumina.utils.rippleClickable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SelectedEditedPhotoSection(
    modifier: Modifier = Modifier,
    selectedEditedPhoto: EditedPhoto?,
    onUnSelectedEditedPhoto: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    if(selectedEditedPhoto == null) return
    val bitmap = remember(selectedEditedPhoto.uri) {
        decodeBitmapFromPath(selectedEditedPhoto.uri)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x3A000000))
            .rippleClickable {
                onUnSelectedEditedPhoto()
            },
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        bitmap?.let { bit ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onUnSelectedEditedPhoto,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    shapes = IconButtonDefaults.shapes(),
                    modifier = Modifier
                        .size(IconButtonDefaults.extraSmallContainerSize())
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                        contentDescription = null,
                        modifier = Modifier.size(IconButtonDefaults.extraSmallIconSize)
                    )
                }
            }
            with(sharedTransitionScope) {
                Image(
                    bitmap = bit.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(
                                key = "editedImage_${selectedEditedPhoto.id}"
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween(durationMillis = 500, easing = FastOutLinearInEasing)
                            })
                )
            }
        }
    }
}