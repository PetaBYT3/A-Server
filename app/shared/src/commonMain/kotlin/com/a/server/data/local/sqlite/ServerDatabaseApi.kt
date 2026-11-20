package com.a.server.data.local.sqlite

import com.a.server.domain.model.sqlite.CellModel
import com.a.server.domain.model.sqlite.FieldModel
import com.a.server.domain.model.sqlite.RowModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel
import kotlinx.serialization.json.JsonObject

interface ServerDatabaseApi {
    suspend fun executeQuery(query: String)
    suspend fun createTable(tableName: String, fields: List<FieldModel>)
    suspend fun deleteTable(names: List<String>)
    suspend fun getRows(tableWithFields: TableWithFieldsModel): List<RowModel>
    suspend fun getRow(tableName: String, rowId: String, fields: List<FieldModel>): List<CellModel>
    suspend fun upsertRow(tableName: String, cells: List<CellModel>)
    suspend fun deleteRow(tableName: String, columnName: String, rowIds: List<String>)

    suspend fun getDynamicRows(tableName: String): List<JsonObject>
}