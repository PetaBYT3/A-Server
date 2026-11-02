package com.a.server.data.local.room

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import com.a.server.core.type.SqlDataType

@Entity(
    tableName = "field",
    foreignKeys = [
        ForeignKey(
            entity = TableEntity::class,
            parentColumns = ["name"],
            childColumns = ["table_name"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FieldEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo("table_name")
    val tableName: String,
    val primary: Boolean,
    val name: String,
    val type: SqlDataType,
    @ColumnInfo("default_value")
    val defaultValue: String
)