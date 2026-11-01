package com.a.server.domain.repository

import arrow.core.Either
import com.a.server.domain.model.sqlite.TableModel
import kotlinx.coroutines.flow.Flow

interface TableRepository {
    fun getList(): Flow<Either<String, List<TableModel>>>
    fun getSingle(id: String): Flow<Either<String, TableModel>>
    fun upsert(tableModel: TableModel): Flow<Either<String, String>>
    fun delete(tableNames: List<String>): Flow<Either<String, String>>
}