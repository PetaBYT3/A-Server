package com.a.server.presentation.storage.folderlist

sealed interface FolderListAction {
    data class SearchQuery(val newQuery: String): FolderListAction

    data object ShowUpsertFolderDialog: FolderListAction
    data object DismissUpsertFolderDialog: FolderListAction
    data class NewFolderName(val newName: String): FolderListAction
    data class NewFolderMaxSize(val newSize: Int): FolderListAction
    data object UpsertFolder: FolderListAction
}