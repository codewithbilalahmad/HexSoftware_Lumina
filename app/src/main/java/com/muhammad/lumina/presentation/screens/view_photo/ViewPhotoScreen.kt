package com.muhammad.lumina.presentation.screens.view_photo

import android.app.Activity
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.lumina.R
import com.muhammad.lumina.presentation.navigation.Destinations
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ViewPhotoScreen(
    navHostController: NavHostController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: ViewPhotoViewModel = koinViewModel(),
) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    val isDarkTheme = isSystemInDarkTheme()
    DisposableEffect(Unit) {
        val controller = WindowCompat.getInsetsController(window, view)
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false
        onDispose {
            controller.isAppearanceLightStatusBars = !isDarkTheme
            controller.isAppearanceLightNavigationBars = !isDarkTheme
        }
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.Black, topBar = {
        CenterAlignedTopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = {
                    navHostController.navigateUp()
                }, shapes = IconButtonDefaults.shapes()) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                        contentDescription = null, tint = Color.White
                    )
                }
            }, actions = {
                Box {
                    IconButton(onClick = {
                        viewModel.onAction(ViewPhotoAction.OnToggleOptionsDropdown)
                    }, shapes = IconButtonDefaults.shapes()) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_menu),
                            contentDescription = null, tint = Color.White
                        )
                    }
                    DropdownMenu(
                        expanded = state.showOptionsDropdown,
                        shape = RoundedCornerShape(16.dp),
                        containerColor = MaterialTheme.colorScheme.background,
                        shadowElevation = 4.dp, tonalElevation = 4.dp, onDismissRequest = {
                            viewModel.onAction(ViewPhotoAction.OnToggleOptionsDropdown)
                        }
                    ) {
                        DropdownMenuItem(text = {
                            Text(text = stringResource(R.string.edit))
                        }, leadingIcon = {
                            Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_edit),contentDescription = null)
                        }, onClick = {
                            viewModel.onAction(ViewPhotoAction.OnToggleOptionsDropdown)
                            navHostController.navigate(Destinations.EditPhotoScreen(state.photoUrl))
                        })
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            state.photoBitmap?.let { bitmap ->
                with(sharedTransitionScope) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(
                                    key = "editedImage_${state.photoUrl}"
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = 300, easing = FastOutLinearInEasing)
                                })
                    )
                }
            }
        }
    }
}