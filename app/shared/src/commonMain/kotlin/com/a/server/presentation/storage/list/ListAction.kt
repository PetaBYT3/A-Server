package com.a.server.presentation.storage.list

sealed interface ListAction {
    data object UpFolder: ListAction
    data class OpenFolder(val folderName: String): ListAction
    data object CreateFolderDialog: ListAction
    data class FolderName(val folderName: String): ListAction
    data object CreateFolder: ListAction
}