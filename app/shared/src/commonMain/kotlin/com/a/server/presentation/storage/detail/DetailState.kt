package com.a.server.presentation.storage.detail

import com.a.server.domain.model.storage.FileModel

data class DetailState(
    val isFileListLoading: Boolean = true,
    val isFileListError: String? = null,
    val fileList: List<FileModel> = emptyList()
)
