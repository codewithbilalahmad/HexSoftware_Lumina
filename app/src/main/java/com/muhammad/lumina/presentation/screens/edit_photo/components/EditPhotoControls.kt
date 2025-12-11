package com.muhammad.lumina.presentation.screens.edit_photo.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.muhammad.lumina.domain.model.EditPhotoFeature
import com.muhammad.lumina.presentation.components.AppSlider

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EditPhotoControls(
    modifier: Modifier = Modifier,
    brightness: Float,
    contrast: Float,
    saturation: Float,
    onBrightnessChange: (Float) -> Unit,
    onContrastChange: (Float) -> Unit,
    onSaturationChange: (Float) -> Unit,
    onSelectFeature: (EditPhotoFeature) -> Unit,
    selectedFeature: EditPhotoFeature?,
) {
    Column(modifier = modifier) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        AnimatedVisibility(
            selectedFeature != null && selectedFeature in listOf(
                EditPhotoFeature.Brightness,
                EditPhotoFeature.Contrast,
                EditPhotoFeature.Saturation
            ),
            enter = slideInVertically(animationSpec = MaterialTheme.motionScheme.slowEffectsSpec()) { -it } + expandVertically(
                animationSpec = MaterialTheme.motionScheme.slowEffectsSpec()
            ) { -it } + fadeIn(animationSpec = MaterialTheme.motionScheme.slowEffectsSpec()),
            exit = slideOutVertically(animationSpec = MaterialTheme.motionScheme.slowEffectsSpec()) { -it } + shrinkVertically(
                animationSpec = MaterialTheme.motionScheme.slowEffectsSpec()
            ) { -it } + fadeOut(animationSpec = MaterialTheme.motionScheme.slowEffectsSpec())
        ) {
            if (selectedFeature != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (selectedFeature) {
                        EditPhotoFeature.Brightness -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = stringResource(selectedFeature.label),
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                                )
                                Text(
                                    text = "${brightness.toInt()}%",
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.surface)
                                )
                            }
                            AppSlider(value = brightness, onValueChange = { newValue ->
                                onBrightnessChange(newValue)
                            }, modifier = Modifier.fillMaxWidth(), valueRange = -100f..100f)
                        }

                        EditPhotoFeature.Contrast -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = stringResource(selectedFeature.label),
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                                )
                                Text(
                                    text = "${(contrast * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.surface)
                                )
                            }
                            AppSlider(value = contrast, onValueChange = { newValue ->
                                onContrastChange(newValue)
                            }, modifier = Modifier.fillMaxWidth(), valueRange = 0f..2f)
                        }
                        EditPhotoFeature.Saturation -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = stringResource(selectedFeature.label),
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                                )
                                Text(
                                    text = "${(saturation * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.surface)
                                )
                            }
                            AppSlider(value = saturation, onValueChange = { newValue ->
                                onSaturationChange(newValue)
                            }, modifier = Modifier.fillMaxWidth(), valueRange = 0f..2f)
                        }
                        else -> Unit
                    }
                }
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(EditPhotoFeature.entries, key = { it.ordinal }) { editFeature ->
                val isSelected = selectedFeature == editFeature
                EditFeatureItem(
                    feature = editFeature,
                    isSelected = isSelected,
                    onClick = { feature ->
                        onSelectFeature(feature)
                    })
            }
        }
    }
}