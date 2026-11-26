package com.a.server.presentation.storage.folderlist

import com.a.server.domain.model.storage.FolderModel

data class FolderListState(
    val searchQuery: String = "",

    val isFolderListLoading: Boolean = true,
    val isFolderListError: String? = null,
    val folderList: List<FolderModel> = emptyList(),

    val isUpsertFolderDialogVisible: Boolean = false,
    val newFolderName: String = "",
    val newFolderMaxSize: Int = 50
)
