package com.a.server.data.mapper

import com.a.server.data.local.room.TableEntity
import com.a.server.domain.model.sqlite.TableModel

fun TableEntity.toModel(): TableModel {
    return TableModel(
        name = this.name
    )
}

fun TableModel.toEntity(): TableEntity {
    return TableEntity(
        name = this.name
    )
}