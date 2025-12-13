package com.muhammad.lumina.presentation.screens.edit_photo

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.lumina.R
import com.muhammad.lumina.domain.model.EditPhotoFeature
import com.muhammad.lumina.domain.model.PhotoFilter
import com.muhammad.lumina.presentation.components.AppAlertDialog
import com.muhammad.lumina.presentation.components.TransparentGridBackground
import com.muhammad.lumina.presentation.screens.edit_photo.components.EditPhotoControls
import com.muhammad.lumina.utils.ObserveAsEvents
import com.muhammad.lumina.utils.SnackbarEvent
import com.muhammad.lumina.utils.createBeautifulGradient
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EditPhotoScreen(
    navHostController: NavHostController,
    viewModel: EditPhotoViewModel = koinViewModel(),
) {
    val density = LocalDensity.current
    val infiniteTransition = rememberInfiniteTransition()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var editPhotoWidth by remember { mutableStateOf(0.dp) }
    var editPhotoHeight by remember { mutableStateOf(0.dp) }
    val scope = rememberCoroutineScope()
    BackHandler {
        viewModel.onAction(EditPhotoAction.OnToggleExitEditingDialog)
    }
    ObserveAsEvents(viewModel.events, onEvent = { event ->
        when (event) {
            EditPhotoEvents.OnNavigateUp -> {
                navHostController.navigateUp()
            }
        }
    })
    ObserveAsEvents(viewModel.snackbarEvents, onEvent = { event ->
        when (event) {
            is SnackbarEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }
            }
        }
    })
    Scaffold(modifier = Modifier.fillMaxSize(), snackbarHost = {
        SnackbarHost(snackbarHostState)
    }, topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(R.string.edit_photo))
            },
            navigationIcon = {
                IconButton(onClick = {
                    viewModel.onAction(EditPhotoAction.OnToggleExitEditingDialog)
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                        contentDescription = null
                    )
                }
            }, actions = {
                IconButton(onClick = {
                    viewModel.onAction(EditPhotoAction.OnToggleSavePhotoToGalleryDialog)
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_save),
                        contentDescription = null
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )
    }, bottomBar = {
        EditPhotoControls(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding(),
            onSelectFeature = { feature ->
                viewModel.onAction(EditPhotoAction.OnSelectEditFeature(feature))
                when (feature) {
                    EditPhotoFeature.RotateLeft -> {
                        viewModel.onAction(EditPhotoAction.OnRotate(-30f))
                    }

                    EditPhotoFeature.RotateRight -> {
                        viewModel.onAction(EditPhotoAction.OnRotate(30f))
                    }

                    EditPhotoFeature.FlipHorizontal -> {
                        viewModel.onAction(EditPhotoAction.OnToggleFlipHorizontal)
                    }

                    EditPhotoFeature.FlipVertical -> {
                        viewModel.onAction(EditPhotoAction.OnToggleFlipVertical)
                    }

                    EditPhotoFeature.Reset -> {
                        viewModel.onAction(EditPhotoAction.OnResetAllEdits)
                    }

                    else -> Unit
                }
            },
            saturation = state.saturation,
            brightness = state.brightness,
            contrast = state.contrast,
            onSaturationChange = { newValue ->
                viewModel.onAction(EditPhotoAction.OnSetSaturation(newValue))
            },
            onContrastChange = { newValue ->
                viewModel.onAction(EditPhotoAction.OnSetContrast(newValue))
            },
            onBrightnessChange = { newValue ->
                viewModel.onAction(EditPhotoAction.OnSetBrightness(newValue))
            },
            selectedFeature = state.selectedFeature,
            selectedPhotoFilter = state.selectedPhotoFilter,
            onPhotoFilterSelected = { filter ->
                viewModel.onAction(EditPhotoAction.OnSetPhotoFilter(filter))
            },
            originalBitmap = state.originalBitmap
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            TransparentGridBackground(modifier = Modifier.fillMaxSize())
            AnimatedVisibility(
                visible = state.editedBitmap != null,
                enter = scaleIn(MaterialTheme.motionScheme.slowEffectsSpec()),
                exit = scaleOut(MaterialTheme.motionScheme.slowEffectsSpec()),
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                state.editedBitmap?.let { bitmap ->
                    val sparkleRotation by infiniteTransition.animateFloat(
                        initialValue = 0f, targetValue = 720f, animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 1500,
                                easing = FastOutLinearInEasing
                            ), repeatMode = RepeatMode.Reverse
                        ), label = "sparkleRotation"
                    )
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onSizeChanged { size ->
                                editPhotoWidth = with(density) {
                                    size.width.toDp()
                                }
                                editPhotoHeight = with(density) {
                                    size.height.toDp()
                                }
                            }
                    )
                    AnimatedVisibility(
                        visible = state.selectedPhotoFilter != PhotoFilter.NORMAL,
                        enter = scaleIn(MaterialTheme.motionScheme.slowEffectsSpec()),
                        exit = scaleOut(MaterialTheme.motionScheme.slowEffectsSpec()),
                    ) {
                        Box(
                            modifier = Modifier.size(
                                width = editPhotoWidth,
                                height = editPhotoHeight
                            ).clipToBounds()
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_sparkle),
                                contentDescription = null, tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                                    .size(24.dp)
                                    .graphicsLayer {
                                        rotationZ = sparkleRotation
                                    }
                            )
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_sparkle),
                                contentDescription = null, tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.BottomStart)
                                    .size(30.dp)
                                    .graphicsLayer {
                                        rotationZ = sparkleRotation
                                    }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = state.editedBitmap != null,
                enter = scaleIn(MaterialTheme.motionScheme.slowEffectsSpec()),
                exit = scaleOut(MaterialTheme.motionScheme.slowEffectsSpec()),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            ) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(vertical = 4.dp, horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            viewModel.onAction(EditPhotoAction.OnUndoEdit)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            disabledContainerColor = MaterialTheme.colorScheme.background.copy(0.7f),
                            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                        ),
                        enabled = viewModel.canUndo(),
                        modifier = Modifier.size(IconButtonDefaults.extraSmallContainerSize())
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_undo),
                            contentDescription = null,
                            modifier = Modifier.size(
                                IconButtonDefaults.extraSmallIconSize
                            )
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.onAction(EditPhotoAction.OnRedoEdit)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            disabledContainerColor = MaterialTheme.colorScheme.background.copy(0.7f),
                            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                        ),
                        enabled = viewModel.canRedo(),
                        modifier = Modifier.size(IconButtonDefaults.extraSmallContainerSize())
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_redo),
                            contentDescription = null,
                            modifier = Modifier.size(
                                IconButtonDefaults.extraSmallIconSize
                            )
                        )
                    }
                }
            }
        }
    }
    if (state.showSaveImageToGalleryDialog) {
        AppAlertDialog(
            onDismiss = {
                viewModel.onAction(EditPhotoAction.OnToggleSavePhotoToGalleryDialog)
            },
            titleContent = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_save),
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                    Text(
                        text = stringResource(R.string.save_photo),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }
            },
            message = stringResource(R.string.save_photo_in_gallery_desp),
            confirmText = stringResource(R.string.save),
            dismissText = stringResource(R.string.cancel),
            isConfirmLoading = state.isSavingImageToGallery,
            confirmEnabled = !state.isSavingImageToGallery,
            onConfirmClick = {
                viewModel.onAction(EditPhotoAction.OnSaveImageToGallery)
            }, onDismissClick = {
                viewModel.onAction(EditPhotoAction.OnToggleSavePhotoToGalleryDialog)
            }
        )
    }
    if (state.showExitEditingDialog) {
        AppAlertDialog(
            onDismiss = {
                viewModel.onAction(EditPhotoAction.OnToggleExitEditingDialog)
            },
            titleContent = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_exit),
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                    Text(
                        text = stringResource(R.string.exit_editing),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }
            },
            dismissButtonColor = MaterialTheme.colorScheme.primary,
            confirmButtonColor = MaterialTheme.colorScheme.error,
            message = stringResource(R.string.exit_editing_desp),
            confirmText = stringResource(R.string.exit),
            dismissText = stringResource(R.string.cancel),
            onConfirmClick = {
                viewModel.onAction(EditPhotoAction.OnConfirmExitEditing)
            },
            onDismissClick = {
                viewModel.onAction(EditPhotoAction.OnToggleExitEditingDialog)
            }
        )
    }
}