package com.a.server.data.repository

import arrow.core.Either
import com.a.server.core.util.catchAndDispatch
import com.a.server.data.local.room.ApplicationDatabase
import com.a.server.data.local.sqlite.ServerDatabaseApi
import com.a.server.data.mapper.toEntity
import com.a.server.data.mapper.toModel
import com.a.server.domain.model.sqlite.CellModel
import com.a.server.domain.model.sqlite.TableCompleteModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel
import com.a.server.domain.repository.TableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class TableRepositoryImpl(
    private val applicationDatabase: ApplicationDatabase,
    private val serverDatabaseApi: ServerDatabaseApi
): TableRepository {
    override fun executeSql(query: String): Flow<Either<String, String>> {
        return flow {
            if (query.isBlank()) {
                emit(Either.Left("Query cannot be blank"))
                return@flow
            }

            serverDatabaseApi.executeQuery(query)
            emit(Either.Right("Query executed"))
        }.catchAndDispatch()
    }

    override fun getTables(): Flow<Either<String, List<TableWithFieldsModel>>> {
        return flow {
            applicationDatabase.tableDao.getList().collect { tableWithFieldsEntities ->
                val tableWithFieldsModels = tableWithFieldsEntities.map { it.toModel() }
                emit(Either.Right(tableWithFieldsModels))
            }
        }.catchAndDispatch()
    }

    override fun getTable(name: String): Flow<Either<String, TableCompleteModel>> {
        return flow<Either<String, TableCompleteModel>> {
            val tableWithFieldsModel = applicationDatabase.tableDao.getSingle(name)
                .filterNotNull()
                .first()
                .toModel()

            val tableCompleteModel = TableCompleteModel(
                tableWithFields = tableWithFieldsModel,
                rows = serverDatabaseApi.getRows(tableWithFieldsModel)
            )

            emit(Either.Right(tableCompleteModel))
        }.catchAndDispatch()
    }

    override fun upsertTable(tableWithFieldsModel: TableWithFieldsModel): Flow<Either<String, String>> {
        return flow<Either<String, String>> {
            applicationDatabase.tableDao.upsert(
                entity = tableWithFieldsModel.table.toEntity()
            )
            applicationDatabase.fieldDao.upsert(
                fieldEntities = tableWithFieldsModel.fields.map { fieldModel ->
                    fieldModel.copy(tableName = tableWithFieldsModel.table.name).toEntity()
                }
            )
            serverDatabaseApi.createTable(
                tableName = tableWithFieldsModel.table.name,
                fields = tableWithFieldsModel.fields
            )
            emit(Either.Right("Table created"))
        }.catchAndDispatch()
    }

    override fun deleteTable(tableNames: List<String>): Flow<Either<String, String>> {
        return flow {
            applicationDatabase.tableDao.delete(
                names = tableNames
            )
            emit(Either.Right("Tables deleted"))
        }.catchAndDispatch()
    }

    override fun getRow(tableName: String, rowId: String): Flow<Either<String, List<CellModel>>> {
        return flow<Either<String, List<CellModel>>> {
            val tableModel = applicationDatabase.tableDao.getSingle(
                name = tableName
            ).filterNotNull().first().toModel()

            if (rowId.isNotBlank()) {
                val cells = serverDatabaseApi.getRow(
                    tableName = tableName,
                    rowId = rowId,
                    fields = tableModel.fields
                )
                emit(Either.Right(cells))
            } else {
                val cells = tableModel.fields.map { fieldModel ->
                    CellModel(
                        columnName = fieldModel.name,
                        type = fieldModel.type,
                        value = ""
                    )
                }
                emit(Either.Right(cells))
            }
        }.catchAndDispatch()
    }

    override fun upsertRow(
        tableName: String,
        cells: List<CellModel>
    ): Flow<Either<String, String>> {
        return flow<Either<String, String>> {
            serverDatabaseApi.upsertRow(
                tableName = tableName,
                cells = cells
            )
            emit(Either.Right("Row upserted"))
        }.catchAndDispatch()
    }
}