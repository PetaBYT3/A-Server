package com.a.server.data.local.room.table

import androidx.room3.Embedded
import androidx.room3.Relation
import com.a.server.data.local.room.field.FieldEntity

data class TableWithFieldsEntity(
    @Embedded
    val table: TableEntity,
    @Relation(
        parentColumns = ["name"],
        entityColumns = ["table_name"]
    )
    val fields: List<FieldEntity>
)
