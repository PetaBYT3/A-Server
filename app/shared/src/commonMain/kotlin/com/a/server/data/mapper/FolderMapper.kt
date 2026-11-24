package com.a.server.data.mapper

import com.a.server.data.local.room.storage.FolderEntity
import com.a.server.domain.model.storage.FolderModel

fun FolderEntity.toModel(): FolderModel {
    return FolderModel(
        name = this.name,
        maxFileSizeMegabyte = this.maxFileSizeMegaByte
    )
}

fun FolderModel.toEntity(): FolderEntity {
    return FolderEntity(
        name = this.name,
        maxFileSizeMegaByte = this.maxFileSizeMegabyte
    )
}