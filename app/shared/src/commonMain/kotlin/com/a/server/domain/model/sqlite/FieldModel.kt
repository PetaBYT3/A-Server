package com.a.server.domain.model.sqlite

import com.a.server.core.type.SqlDataType

data class FieldModel(
    val id: String,
    val tableName: String,
    val primary: Boolean,
    val name: String,
    val type: SqlDataType,
    val defaultValue: String
)
