package com.a.server.data.local.room

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "server_table_metadata")
data class ServerTableEntity(
    @PrimaryKey
    val tableName: String,
    val fieldsDefinitionJson: String
)
