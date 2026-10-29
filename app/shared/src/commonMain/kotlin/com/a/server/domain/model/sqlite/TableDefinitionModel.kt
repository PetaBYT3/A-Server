package com.a.server.domain.model.sqlite

data class TableDefinitionModel(
    val tableName: String,
    val fields: List<TableFieldModel>
)
