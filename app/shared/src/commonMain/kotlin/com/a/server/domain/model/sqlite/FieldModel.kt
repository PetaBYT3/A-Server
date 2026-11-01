package com.a.server.domain.model.sqlite

import kotlinx.serialization.Serializable

@Serializable
data class FieldModel(
    val primary: Boolean,
    val name: String,
    val type: DataType
)
