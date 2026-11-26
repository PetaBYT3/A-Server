package com.a.server.domain.model.storage

data class FileModel(
    val name: String,
    val size: String,
    val absolutePath: String,
    val localDirectUrl: String,
    val remoteDirectUrl: String
)
