package com.a.server.presentation.storage.list

import com.a.server.domain.model.storage.FolderModel

data class ListState(
    val searchQuery: String = "",

    val isFolderListLoading: Boolean = true,
    val isFolderListError: String? = null,
    val folderList: List<FolderModel> = emptyList(),

    val isUpsertFolderDialogVisible: Boolean = false,
    val newFolderName: String = "",
    val newFolderMaxSize: Int = 50
)
