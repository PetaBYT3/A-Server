package com.a.server.data.local.room

import androidx.room3.Embedded
import androidx.room3.Relation

data class TableWithFieldsEntity(
    @Embedded
    val table: TableEntity,
    @Relation(
        parentColumns = ["name"],
        entityColumns = ["table_name"]
    )
    val fields: List<FieldEntity>
)
