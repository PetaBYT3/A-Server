package com.a.server.data.repository

import arrow.core.Either
import com.a.server.core.util.catchAndDispatch
import com.a.server.data.local.room.ApplicationDatabase
import com.a.server.data.local.sqlite.ServerDatabaseApi
import com.a.server.data.mapper.TableMapper
import com.a.server.domain.model.sqlite.TableModel
import com.a.server.domain.repository.TableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class TableRepositoryImpl(
    private val applicationDatabase: ApplicationDatabase,
    private val serverDatabaseApi: ServerDatabaseApi
): TableRepository {
    override fun getList(): Flow<Either<String, List<TableModel>>> {
        return flow {
            applicationDatabase.tableDao.getList().collect { tableEntities ->
                val tableModels = tableEntities.map { TableMapper.toModel(it) }
                emit(Either.Right(tableModels))
            }
        }.catchAndDispatch()
    }

    override fun getSingle(id: String): Flow<Either<String, TableModel>> {
        return flow<Either<String, TableModel>> {

        }.catchAndDispatch()
    }

    override fun upsert(tableModel: TableModel): Flow<Either<String, String>> {
        return flow<Either<String, String>> {
            serverDatabaseApi.createTable(
                tableName = tableModel.tableName,
                fields = tableModel.fields
            )
            applicationDatabase.tableDao.upsert(
                entity = TableMapper.toDto(tableModel)
            )
            emit(Either.Right("Table created"))
        }.catchAndDispatch()
    }

    override fun delete(tableNames: List<String>): Flow<Either<String, String>> {
        return flow {
            applicationDatabase.tableDao.delete(
                tableNames = tableNames
            )
            emit(Either.Right("Tables deleted"))
        }.catchAndDispatch()
    }
}