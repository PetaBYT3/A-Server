package com.a.server.data.local.room.storage

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "folder")
data class FolderEntity(
    @PrimaryKey
    val name: String,
    val maxFileSizeMegaByte: Int = 50
)
