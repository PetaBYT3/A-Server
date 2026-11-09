package com.a.server.presentation.storage.detail

import com.a.server.domain.model.storage.FileModel

data class DetailState(
    val isFileLoading: Boolean = true,
    val isFileError: String? = null,
    val file: FileModel? = null
)
