package com.a.server.presentation.auth

sealed interface AuthAction {
    data class Search(val newSearch: String): AuthAction
    data class Select(val newId: String): AuthAction

    data object DeleteAlertDialog: AuthAction
    data object Delete: AuthAction
}