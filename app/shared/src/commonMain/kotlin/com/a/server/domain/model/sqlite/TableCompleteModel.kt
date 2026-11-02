package com.a.server.domain.model.sqlite

data class TableCompleteModel(
    val tableWithFields: TableWithFieldsModel,
    val rows: List<RowModel>
)