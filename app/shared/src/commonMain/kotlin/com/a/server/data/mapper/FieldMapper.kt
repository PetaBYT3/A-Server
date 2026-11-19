package com.a.server.data.mapper

import com.a.server.data.local.room.field.FieldEntity
import com.a.server.domain.model.sqlite.FieldModel

fun FieldEntity.toModel(): FieldModel {
    return FieldModel(
        id = this.id,
        tableName = this.tableName,
        primary = this.primary,
        name = this.name,
        type = this.type,
        defaultValue = this.defaultValue
    )
}

fun FieldModel.toEntity(): FieldEntity {
    return FieldEntity(
        id = this.id,
        tableName = this.tableName,
        primary = this.primary,
        name = this.name,
        type = this.type,
        defaultValue = this.defaultValue
    )
}