package com.a.server.data.local.room

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "server_table_metadata")
data class TableEntity(
    @PrimaryKey
    @ColumnInfo("table_name")
    val tableName: String,
    val fields: String
)
