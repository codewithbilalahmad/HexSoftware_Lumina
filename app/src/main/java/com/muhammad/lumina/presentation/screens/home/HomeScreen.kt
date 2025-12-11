package com.muhammad.lumina.presentation.screens.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.lumina.R
import com.muhammad.lumina.presentation.navigation.Destinations
import com.muhammad.lumina.presentation.screens.home.components.AppFeaturesSection
import com.muhammad.lumina.presentation.screens.home.components.AppIntroSection
import com.muhammad.lumina.presentation.screens.home.components.EditedPhotosSection
import com.muhammad.lumina.presentation.screens.home.components.ImageStackSection
import com.muhammad.lumina.presentation.screens.home.components.PickPhotoSection
import com.muhammad.lumina.presentation.screens.home.components.SelectedEditedPhotoSection
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.onAction(HomeAction.OnPhotoSelected(uri.toString()))
            }
        }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_logo),
                            contentDescription = null, modifier = Modifier.size(30.dp)
                        )
                        Text(text = stringResource(R.string.app_name))
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_settings),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = paddingValues
            ) {
                item("ImageStackSection") {
                    ImageStackSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                    )
                }
                item("AppIntroSection") {
                    AppIntroSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                    )
                }
                if (state.editedPhotos.isNotEmpty()) {
                    item("EditedPhotosSection") {
                        EditedPhotosSection(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                            onEditedPhotoClick = { editedPhoto ->
                                viewModel.onAction(HomeAction.OnEditedPhotoSelected(editedPhoto))
                            },
                            animatedVisibilityScope = animatedVisibilityScope,
                            sharedTransitionScope = sharedTransitionScope,
                            editedPhotos = state.editedPhotos
                        )
                    }
                }
                item("AppFeaturesSection") {
                    AppFeaturesSection(
                        features = state.appFeatures,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                    )
                }
                item("PickPhotoSection") {
                    PickPhotoSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        selectedPhoto = state.selectedPhotoBitmap,
                        onPickImage = {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        onRemoveImage = {
                            viewModel.onAction(HomeAction.OnClearPhoto)
                        },
                        onEditImageClick = {
                            viewModel.onAction(HomeAction.OnClearPhoto)
                            navHostController.navigate(Destinations.EditPhotoScreen(state.selectedPhoto.orEmpty()))
                        })
                }
            }
        }
        SelectedEditedPhotoSection(
            onUnSelectedEditedPhoto = {
                viewModel.onAction(HomeAction.OnEditedPhotoSelected(null))
            }, modifier = Modifier.fillMaxSize(),
            selectedEditedPhoto = state.selectedEditedPhoto,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope
        )
    }
}