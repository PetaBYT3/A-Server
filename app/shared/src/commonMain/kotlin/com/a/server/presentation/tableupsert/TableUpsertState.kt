package com.a.server.presentation.tableupsert

import com.a.server.core.type.SqlDataType
import com.a.server.domain.model.sqlite.FieldModel
import com.a.server.domain.model.sqlite.TableModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel
import ulid.ULID

data class TableUpsertState(
    val tableWithFields: TableWithFieldsModel = TableWithFieldsModel(
        table = TableModel(
            name = ""
        ),
        fields = listOf(
            FieldModel(
                id = ULID.randomULID(),
                tableName = "",
                primary = true,
                name = "id",
                type = SqlDataType.TEXT,
                defaultValue = "Random ULID"
            )
        )
    )
)
