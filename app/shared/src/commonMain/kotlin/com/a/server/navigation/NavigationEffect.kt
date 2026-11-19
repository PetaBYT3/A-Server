package com.a.server.navigation

sealed interface NavigationEffect {
    data class ShowSnackBar(val message: String): NavigationEffect
    data object NavigateBack: NavigationEffect
}