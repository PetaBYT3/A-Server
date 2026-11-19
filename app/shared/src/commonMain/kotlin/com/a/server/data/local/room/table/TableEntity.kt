package com.a.server.data.local.room.table

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "table")
data class TableEntity(
    @PrimaryKey
    val name: String,
    @ColumnInfo("position_x")
    val positionX: Float = 0f,
    @ColumnInfo("position_y")
    val positionY: Float = 0f
)
