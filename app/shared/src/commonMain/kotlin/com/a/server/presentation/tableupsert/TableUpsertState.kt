package com.a.server.presentation.tableupsert

import com.a.server.domain.model.sqlite.DataType
import com.a.server.domain.model.sqlite.FieldModel

data class TableUpsertState(
    val tableName: String = "",
    val fieldList: List<FieldModel> = listOf(
        FieldModel(
            primary = true,
            name = "id",
            type = DataType.TEXT
        )
    )
)
