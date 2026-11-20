package com.a.server.presentation.database.upserttable

import androidx.compose.ui.geometry.Offset
import com.a.server.core.type.DataType
import com.a.server.domain.model.sqlite.FieldModel
import com.a.server.domain.model.sqlite.TableModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel
import ulid.ULID

data class UpsertState(
    val tableWithFields: TableWithFieldsModel = TableWithFieldsModel(
        table = TableModel(
            name = "",
            position = Offset(0f, 0f)
        ),
        fields = listOf(
            FieldModel(
                id = ULID.randomULID(),
                tableName = "",
                primary = true,
                name = "id",
                type = DataType.TEXT,
                defaultValue = "Random ULID"
            )
        )
    )
)
