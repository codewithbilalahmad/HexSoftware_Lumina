package com.muhammad.lumina.presentation.components.snackbar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.lumina.domain.model.GradientSnackbarVisuals
import com.muhammad.lumina.utils.rippleClickable
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun GradientSnackbar(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit, dismissThreshold: Dp = 120.dp,
    snackbarVisuals: GradientSnackbarVisuals, onActionClick: () -> Unit = {},
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val dismissThresholdPx = with(density) {
        dismissThreshold.toPx()
    }
    val offsetX = remember {
        Animatable(0f)
    }
    val draggableState = rememberDraggableState { delta ->
        scope.launch {
            offsetX.snapTo(offsetX.value + delta)
        }
    }
    val dismissAlpha = (1f - (abs(offsetX.value) / dismissThresholdPx)).coerceIn(0f, 1f)
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "scale"
    )
    Card(
        modifier = modifier
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
                this.translationX = offsetX.value
                this.alpha = dismissAlpha
            }
            .fillMaxWidth(0.95f)
            .padding(8.dp)
            .draggable(
                orientation = Orientation.Horizontal,
                state = draggableState, onDragStopped = { velocity ->
                    if (abs(offsetX.value) >= dismissThresholdPx) {
                        val target =
                            if (offsetX.value > 0) dismissThresholdPx * 2 else -dismissThresholdPx * 2
                        scope.launch {
                            offsetX.animateTo(
                                targetValue = target, animationSpec = tween(200)
                            )
                            onDismiss()
                        }
                    } else {
                        scope.launch {
                            offsetX.animateTo(
                                targetValue = 0f, animationSpec = spring(
                                    stiffness = Spring.StiffnessMedium,
                                    dampingRatio = Spring.DampingRatioMediumBouncy
                                )
                            )
                        }
                    }
                }
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(0.90f),
                            MaterialTheme.colorScheme.primary.copy(0.85f),
                        )
                    )
                )
                .then(
                    if (snackbarVisuals.actionLabel == null) Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 12.dp
                    ) else Modifier.padding(8.dp)
                )
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(snackbarVisuals.icon),
                    contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = snackbarVisuals.message,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimary)
                )
            }
            snackbarVisuals.actionLabel?.let { label ->
                Text(
                    text = label,
                    modifier = Modifier.rippleClickable(onClick = onActionClick),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}