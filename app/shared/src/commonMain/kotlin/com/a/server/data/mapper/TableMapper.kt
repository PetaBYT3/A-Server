package com.a.server.data.mapper

import com.a.server.data.local.room.TableEntity
import com.a.server.domain.model.sqlite.TableModel
import kotlinx.serialization.json.Json

object TableMapper: DataMapper<TableEntity, TableModel> {
    override fun toModel(dto: TableEntity): TableModel {
        return TableModel(
            tableName = dto.tableName,
            fields = Json.decodeFromString(dto.fields)
        )
    }

    override fun toDto(model: TableModel): TableEntity {
        return TableEntity(
            tableName = model.tableName,
            fields = Json.encodeToString(model.fields)
        )
    }
}