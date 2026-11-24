package com.a.server.presentation.storage.list

sealed interface ListAction {
    data class SearchQuery(val newQuery: String): ListAction

    data object UpsertFolderDialog: ListAction
    data class NewFolderName(val newName: String): ListAction
    data class NewFolderMaxSize(val newSize: Int): ListAction
    data object UpsertFolder: ListAction
}