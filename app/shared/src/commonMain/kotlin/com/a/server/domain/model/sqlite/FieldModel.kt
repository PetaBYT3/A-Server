package com.a.server.domain.model.sqlite

import com.a.server.core.type.DataType

data class FieldModel(
    val id: String,
    val tableName: String,
    val primary: Boolean,
    val name: String,
    val type: DataType,
    val defaultValue: String
)
