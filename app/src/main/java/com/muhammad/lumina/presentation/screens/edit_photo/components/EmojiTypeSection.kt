package com.muhammad.lumina.presentation.screens.edit_photo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammad.lumina.domain.model.Emoji
import com.muhammad.lumina.domain.model.EmojiType

@Composable
fun EmojiTypeSection(
    modifier: Modifier = Modifier,
    type: EmojiType,
    emojis: List<Emoji>,
    onEmojiPick: (Emoji) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(type.label), modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.surface)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 8,
            horizontalArrangement = Arrangement.Center
        ) {
            emojis.forEach { emoji ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = {
                            onEmojiPick(emoji)
                        })
                        .padding(4.dp), contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji.emoji, fontSize = 28.sp)
                }
            }
        }
    }
}