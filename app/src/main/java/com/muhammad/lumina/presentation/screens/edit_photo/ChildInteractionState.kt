package com.muhammad.lumina.presentation.screens.edit_photo

sealed interface ChildInteractionState {
    data object None : ChildInteractionState
    data class Selected(val id: String) : ChildInteractionState
    data class Editing(val id: String) : ChildInteractionState
}