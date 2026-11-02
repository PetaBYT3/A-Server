package com.a.server.domain.repository

import arrow.core.Either
import com.a.server.domain.model.sqlite.CellModel
import com.a.server.domain.model.sqlite.TableCompleteModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel
import kotlinx.coroutines.flow.Flow

interface TableRepository {
    fun executeSql(query: String): Flow<Either<String, String>>
    fun getTables(): Flow<Either<String, List<TableWithFieldsModel>>>
    fun getTable(name: String): Flow<Either<String, TableCompleteModel>>
    fun upsertTable(tableWithFieldsModel: TableWithFieldsModel): Flow<Either<String, String>>
    fun deleteTable(tableNames: List<String>): Flow<Either<String, String>>
    fun getRow(tableName: String, rowId: String): Flow<Either<String, List<CellModel>>>
    fun upsertRow(tableName: String, cells: List<CellModel>): Flow<Either<String, String>>
}