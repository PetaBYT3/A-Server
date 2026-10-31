package com.a.server.presentation.authentication

sealed interface AuthenticationAction {
    data class Search(val newSearch: String): AuthenticationAction
    data class Select(val newId: String): AuthenticationAction

    data object DeleteAlertDialog: AuthenticationAction
    data object Delete: AuthenticationAction
}