package com.a.server.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "server_table_metadata")
data class ServerTableEntity(
    @PrimaryKey
    val tableName: String,
    val fieldsDefinitionJson: String
)
