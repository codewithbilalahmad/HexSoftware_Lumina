package com.muhammad.lumina.presentation.screens.edit_photo.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.lumina.domain.model.EditPhotoFeature
import com.muhammad.lumina.utils.rippleClickable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EditFeatureItem(
    modifier: Modifier = Modifier,
    feature: EditPhotoFeature,
    onClick: (EditPhotoFeature) -> Unit,
    isSelected: Boolean,
) {
    val background by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "color"
    )
    val labelColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "contentColor"
    )
    val iconTint by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "contentColor"
    )
    val shape =
        if (isSelected) MaterialShapes.Cookie9Sided.toShape() else MaterialShapes.Circle.toShape()
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
    Column(
        modifier = modifier
            .wrapContentSize()
            .rippleClickable {
                onClick(feature)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(background)
                .padding(12.dp), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(feature.icon),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = stringResource(feature.label),
            style = MaterialTheme.typography.bodySmall.copy(
                color = labelColor,
                fontWeight = fontWeight
            )
        )
    }
}