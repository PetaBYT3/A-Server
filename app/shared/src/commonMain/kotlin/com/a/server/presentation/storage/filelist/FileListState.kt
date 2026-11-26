package com.a.server.presentation.storage.filelist

import com.a.server.domain.model.storage.FileModel

data class FileListState(
    val isFileListLoading: Boolean = true,
    val isFileListError: String? = null,
    val fileList: List<FileModel> = emptyList(),

    val selectedFile: List<FileModel> = emptyList()
)
