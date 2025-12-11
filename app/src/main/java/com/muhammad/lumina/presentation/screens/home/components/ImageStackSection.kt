package com.muhammad.lumina.presentation.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.muhammad.lumina.R

@Composable
fun ImageStackSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.person1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                    rotationZ = -16f
                }
                .clip(RoundedCornerShape(24.dp))
                .weight(1f)
                .height(height = 150.dp)
                .border(2.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(24.dp))
        )
        Image(
            painter = painterResource(R.drawable.person2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                .clip(RoundedCornerShape(24.dp))
                .weight(1.5f)
                .height(height = 150.dp)
                .border(2.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(24.dp))
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
        )
        Image(
            painter = painterResource(R.drawable.person3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                    rotationZ = 16f
                }
                .clip(RoundedCornerShape(24.dp))
                .weight(1f)
                .height(height = 150.dp)
                .border(2.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(24.dp))
        )
    }
}