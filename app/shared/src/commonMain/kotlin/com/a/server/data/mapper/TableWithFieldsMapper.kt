package com.a.server.data.mapper

import com.a.server.data.local.room.TableWithFieldsEntity
import com.a.server.domain.model.sqlite.TableWithFieldsModel

fun TableWithFieldsEntity.toModel(): TableWithFieldsModel {
    return TableWithFieldsModel(
        table = this.table.toModel(),
        fields = this.fields.map { it.toModel() }
    )
}

fun TableWithFieldsModel.toEntity(): TableWithFieldsEntity {
    return TableWithFieldsEntity(
        table = this.table.toEntity(),
        fields = this.fields.map { it.toEntity() }
    )
}