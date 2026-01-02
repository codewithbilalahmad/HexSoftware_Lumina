package com.muhammad.lumina.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Destinations {
    @Serializable
    data object HomeScreen : Destinations

    @Serializable
    data class EditPhotoScreen(val photo: String) : Destinations

    @Serializable
    data class ViewPhotoScreen(val photo : String) : Destinations
}