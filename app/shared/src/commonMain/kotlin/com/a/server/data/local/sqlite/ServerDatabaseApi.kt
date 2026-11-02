package com.a.server.data.local.sqlite

import com.a.server.domain.model.sqlite.CellModel
import com.a.server.domain.model.sqlite.FieldModel
import com.a.server.domain.model.sqlite.RowModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel

interface ServerDatabaseApi {
    suspend fun executeQuery(query: String)
    suspend fun createTable(tableName: String, fields: List<FieldModel>)
    suspend fun deleteTable(tableName: String)
    suspend fun getRows(tableWithFields: TableWithFieldsModel): List<RowModel>
    suspend fun getRow(tableName: String, rowId: String, fields: List<FieldModel>): List<CellModel>
    suspend fun upsertRow(tableName: String, cells: List<CellModel>)
}