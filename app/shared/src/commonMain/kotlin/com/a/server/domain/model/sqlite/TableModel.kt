package com.a.server.domain.model.sqlite

data class TableModel(
    val tableName: String,
    val fields: List<FieldModel>
)
