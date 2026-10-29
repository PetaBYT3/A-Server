package com.a.server.data.local.sqlite

import com.a.server.domain.model.sqlite.TableFieldModel

interface ServerDatabaseApi {
    suspend fun executeQuery(query: String)
    suspend fun createTable(tableName: String, fields: List<TableFieldModel>)
    suspend fun upsert(tableName: String, data: Map<String, Any?>)
    suspend fun delete(tableName: String, id: Long)
}