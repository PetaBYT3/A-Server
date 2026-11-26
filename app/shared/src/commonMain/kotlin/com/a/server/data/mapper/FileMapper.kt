package com.a.server.data.mapper

import com.a.server.core.util.toReadableSize
import com.a.server.domain.model.storage.FileModel
import java.io.File

fun File.toFileModel(
    localDirectUrl: String,
    remoteDirectUrl: String
): FileModel {
    return FileModel(
        name = this.name,
        size = this.length().toReadableSize(),
        absolutePath = this.absolutePath,
        localDirectUrl = "$localDirectUrl/${this.name}",
        remoteDirectUrl = remoteDirectUrl
    )
}

fun Array<out File>?.toSortedFileModel(
    localDirectUrl: String,
    remoteDirectUrl: String
): List<FileModel> {
    if (this == null) {
        return emptyList()
    }

    val fileModels = this.map { file ->
        FileModel(
            name = file.name,
            size = file.length().toReadableSize(),
            absolutePath = file.absolutePath,
            localDirectUrl = "$localDirectUrl/${file.name}",
            remoteDirectUrl = remoteDirectUrl
        )
    }

    return fileModels
}