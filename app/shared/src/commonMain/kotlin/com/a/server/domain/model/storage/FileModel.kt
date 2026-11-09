package com.a.server.domain.model.storage

data class FileModel(
    val name: String,
    val isFolder: Boolean,
    val size: String,
    val absolutePath: String
)
