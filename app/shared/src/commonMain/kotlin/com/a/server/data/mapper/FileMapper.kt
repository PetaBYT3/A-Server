package com.a.server.data.mapper

import com.a.server.core.util.toReadableSize
import com.a.server.domain.model.storage.FileModel
import java.io.File

fun File.toFileModel(): FileModel {
    return FileModel(
        name = this.name,
        isFolder = !this.isFile,
        size = this.length().toReadableSize(),
        absolutePath = this.absolutePath
    )
}

fun Array<out File>?.toSortedFileModel(): List<FileModel> {
    if (this == null) {
        return emptyList()
    }

    val fileModels = this.map { file ->
        FileModel(
            name = file.name,
            isFolder = !file.isFile,
            size = file.length().toReadableSize(),
            absolutePath = file.absolutePath
        )
    }

    val folder = fileModels.filter { it.isFolder }.sortedBy { it.name }
    val file = fileModels.filter { !it.isFolder }.sortedBy { it.name }

    return folder + file
}