package com.a.server.domain.model.sqlite

import com.a.server.core.type.SqlDataType

data class CellModel(
    val columnName: String,
    val type: SqlDataType,
    val value: String?
)
