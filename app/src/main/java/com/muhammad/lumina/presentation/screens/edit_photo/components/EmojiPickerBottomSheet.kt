package com.muhammad.lumina.presentation.screens.edit_photo.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.lumina.R
import com.muhammad.lumina.domain.model.Emoji
import com.muhammad.lumina.domain.model.EmojiType
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EmojiPickerBottomSheet(
    modifier: Modifier = Modifier,showEmojiPickerBottomSheet : Boolean,
    onPickEmoji: (Emoji) -> Unit, onDismiss: () -> Unit,emojiMap : Map<EmojiType, List<Emoji>>
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectedType by remember{
        derivedStateOf {
            EmojiType.entries.getOrNull(listState.firstVisibleItemIndex)
        }
    }
    if(showEmojiPickerBottomSheet){
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            modifier = modifier.fillMaxWidth(),
            dragHandle = {},
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f), topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 40.dp, height = 5.dp)
                                .clip(CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.surface
                                )
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {

                                },
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .size(IconButtonDefaults.extraSmallContainerSize()),
                                shapes = IconButtonDefaults.shapes(),
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                                    contentDescription = null, tint = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.size(IconButtonDefaults.extraSmallIconSize)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 24.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_emoji),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }, bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 4.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        EmojiType.entries.forEach { type ->
                            val icon = when (type) {
                                EmojiType.SMILEYS_AND_PEOPLE -> R.drawable.ic_emoji
                                EmojiType.ANIMALS_AND_NATURE -> R.drawable.ic_animal
                                EmojiType.FOOD_AND_DRINKS -> R.drawable.ic_food
                                EmojiType.ACTIVITY_AND_SPORTS -> R.drawable.ic_activity
                                EmojiType.TRAVEL_AND_PLACES -> R.drawable.ic_travel
                                EmojiType.OBJECTS -> R.drawable.ic_object
                                EmojiType.SYMBOLS -> R.drawable.ic_symbols
                            }
                            val tint by animateColorAsState(
                                targetValue = if(type == selectedType) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.surface,
                                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                                label = "tint"
                            )
                            IconButton(modifier = Modifier.size(24.dp), onClick = {
                                scope.launch {
                                    listState.animateScrollToItem(type.ordinal)
                                }
                            }){
                                Icon(
                                    imageVector = ImageVector.vectorResource(icon),
                                    contentDescription = null,tint = tint,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues), state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(EmojiType.entries, key = {it.name}, contentType = {
                        "emoji_type_${it.ordinal}"
                    }){type ->
                        val emojis = emojiMap[type] ?: emptyList()
                        EmojiTypeSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                            type = type, onEmojiPick = onPickEmoji,
                            emojis = emojis
                        )
                    }
                }
            }
        }
    }
}