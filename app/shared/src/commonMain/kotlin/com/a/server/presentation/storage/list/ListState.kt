package com.a.server.presentation.storage.list

import com.a.server.domain.model.storage.FileModel

data class ListState(
    val storagePath: String? = null,
    val currentPath: String? = null,

    val isFilesLoading: Boolean = true,
    val files: List<FileModel> = emptyList(),

    val isCreateFolderDialogVisible: Boolean = false,
    val newFolderName: String = ""
)
