package com.a.server.data.local.room.table

import androidx.compose.ui.geometry.Offset
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "table")
data class TableEntity(
    @PrimaryKey
    val name: String,
    val position: Offset
)
