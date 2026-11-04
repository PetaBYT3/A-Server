package com.a.server.presentation.authentication.list

sealed interface ListAction {
    data class Search(val newSearch: String): ListAction
    data class Select(val newId: String): ListAction

    data object DeleteAlertDialog: ListAction
    data object Delete: ListAction
}