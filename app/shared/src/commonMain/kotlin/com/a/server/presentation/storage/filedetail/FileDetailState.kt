package com.a.server.presentation.storage.filedetail

import com.a.server.domain.model.storage.FileModel

data class FileDetailState(
    val isFileLoading: Boolean = true,
    val isFileError: String? = null,
    val file: FileModel? = null
)
