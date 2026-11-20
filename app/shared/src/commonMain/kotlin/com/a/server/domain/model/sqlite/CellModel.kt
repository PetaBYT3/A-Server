package com.a.server.domain.model.sqlite

import com.a.server.core.type.DataType

data class CellModel(
    val columnName: String,
    val isPrimary: Boolean,
    val type: DataType,
    val value: String?
)
