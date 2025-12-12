package com.muhammad.lumina.presentation.screens.edit_photo.components

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.lumina.domain.model.PhotoFilter
import com.muhammad.lumina.utils.applyPhotoFilter

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PhotoFilterItem(
    modifier: Modifier = Modifier, originalBitmap: Bitmap?,
    photoFilter: PhotoFilter, isSelected: Boolean,
    onPhotoFilterSelected: (PhotoFilter) -> Unit,
) {
    if (originalBitmap != null) {
        val filteredBitmap= remember(originalBitmap, photoFilter) {
            applyPhotoFilter(originalBitmap, photoFilter)
        }
        val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        val shape by animateDpAsState(
            targetValue = if (isSelected) 100.dp else 16.dp,
            animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
            label = "shape"
        )
        val labelColor by animateColorAsState(
            targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
            label = "contentColor"
        )
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(modifier = modifier
                .size(80.dp)
                .clip(RoundedCornerShape(shape))
                .clickable {
                    onPhotoFilterSelected(photoFilter)
                }, contentAlignment = Alignment.Center) {
                Image(
                    bitmap = filteredBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(shape)), contentScale = ContentScale.Crop
                )
                if (isSelected) {
                    LoadingIndicator(color = MaterialTheme.colorScheme.background)
                }
            }
            Text(
                text = stringResource(photoFilter.label),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = labelColor,
                    fontWeight = fontWeight
                )
            )
        }
    }
}