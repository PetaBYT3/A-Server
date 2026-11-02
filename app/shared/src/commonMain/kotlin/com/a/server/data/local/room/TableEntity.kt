package com.a.server.data.local.room

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "table")
data class TableEntity(
    @PrimaryKey
    val name: String
)
