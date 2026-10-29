package com.a.server.presentation.util

sealed interface Effect {
    data class ShowSnackBar(val message: String): Effect
}