package com.a.server.domain.model.sqlite

data class TableWithFieldsModel(
    val table: TableModel,
    val fields: List<FieldModel>
)
