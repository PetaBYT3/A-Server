package com.a.server.data.local.sqlite

import androidx.sqlite.SQLiteDriver
import androidx.sqlite.SQLiteStatement
import com.a.server.core.type.SqlDataType
import com.a.server.data.local.platform.DirectoryApi
import com.a.server.domain.model.sqlite.CellModel
import com.a.server.domain.model.sqlite.FieldModel
import com.a.server.domain.model.sqlite.RowModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel
import org.koin.core.annotation.Single

@Single
class ServerDatabaseApiImpl(
    private val directoryApi: DirectoryApi,
    private val sqLiteDriver: SQLiteDriver
): ServerDatabaseApi {
    override suspend fun executeQuery(query: String) {
        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            connection.prepare(query).use { statement ->
                statement.step()
            }
        }
    }

    override suspend fun createTable(
        tableName: String,
        fields: List<FieldModel>
    ) {
        val primaryColumns = fields.filter { it.primary }.joinToString(", ") { field ->
            "${field.name} ${field.type.name} PRIMARY KEY"
        }
        val normalColumns = fields.filter { !it.primary }.joinToString(", ") { field ->
            "${field.name} ${field.type.name}"
        }

        val columnsDef = listOf(primaryColumns, normalColumns)
            .filter { it.isNotEmpty() }
            .joinToString(", ")

        val sql = """
            CREATE TABLE IF NOT EXISTS $tableName ($columnsDef)
        """.trimIndent()

        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            connection.prepare(sql).use { statement ->
                statement.step()
            }
        }
    }

    override suspend fun deleteTable(tableName: String) {
        val sql = "DROP TABLE IF EXISTS $tableName"

        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            connection.prepare(sql).use { statement ->
                statement.step()
            }
        }
    }

    override suspend fun getRows(tableWithFields: TableWithFieldsModel): List<RowModel> {
        val sql = "SELECT * FROM ${tableWithFields.table.name}"
        val rowList = mutableListOf<RowModel>()

        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            connection.prepare(sql).use { statement ->
                while (statement.step()) {
                    val cellList = mutableListOf<CellModel>()

                    tableWithFields.fields.forEachIndexed { index, field ->
                        val cell = CellModel(
                            columnName = field.name,
                            type = field.type,
                            value = getValueAsString(statement, index, field.type)
                        )
                        cellList.add(cell)
                    }
                    rowList.add(RowModel(cellList))
                }
            }
        }
        return rowList
    }

    override suspend fun getRow(
        tableName: String,
        rowId: String,
        fields: List<FieldModel>
    ): List<CellModel> {
        val primaryField = fields.find { it.primary } ?: fields.firstOrNull()
        val primaryColumnName = primaryField?.name ?: "id"

        val sql = "SELECT * FROM `$tableName` WHERE `$primaryColumnName` = ?"
        val cellList = mutableListOf<CellModel>()

        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            connection.prepare(sql).use { statement ->
                statement.bindText(1, rowId)

                if (statement.step()) {
                    fields.forEachIndexed { index, field ->
                        val cell = CellModel(
                            columnName = field.name,
                            type = field.type,
                            value = getValueAsString(statement, index, field.type)
                        )
                        cellList.add(cell)
                    }
                }
            }
        }
        return cellList
    }

    override suspend fun upsertRow(
        tableName: String,
        cells: List<CellModel>
    ) {
        val columns = cells.joinToString(", ") { "`${it.columnName}`" }
        val placeholders = cells.joinToString(", ") { "?" }
        val sql = "INSERT OR REPLACE INTO `$tableName` ($columns) VALUES ($placeholders)"

        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            connection.prepare(sql).use { statement ->
                cells.forEachIndexed { index, cell ->
                    val bindIndex = index + 1
                    when (cell.type) {
                        SqlDataType.INTEGER -> {
                            val longVal = cell.value?.toLongOrNull()
                            if (longVal != null) {
                                statement.bindLong(bindIndex, longVal)
                            } else {
                                statement.bindNull(bindIndex)
                            }
                        }
                        SqlDataType.REAL -> {
                            val doubleVal = cell.value?.toDoubleOrNull()
                            if (doubleVal != null) {
                                statement.bindDouble(bindIndex, doubleVal)
                            } else {
                                statement.bindNull(bindIndex)
                            }
                        }
                        else -> {
                            if (cell.value != null) {
                                statement.bindText(bindIndex, cell.value)
                            } else {
                                statement.bindNull(bindIndex)
                            }
                        }
                    }
                }
                statement.step()
            }
        }
    }

    private fun bindValue(
        statement: SQLiteStatement,
        index: Int,
        value: Any?
    ) {
        when (value) {
            null -> statement.bindNull(index)
            is String -> statement.bindText(index, value)
            is Int -> statement.bindLong(index, value.toLong())
            is Long -> statement.bindLong(index, value)
            is Double -> statement.bindDouble(index, value)
            is Float -> statement.bindDouble(index, value.toDouble())
            is Boolean -> statement.bindLong(index, if (value) 1L else 0L)
            else -> statement.bindText(index, value.toString())
        }
    }

    private fun getValueAsString(
        statement: SQLiteStatement,
        index: Int,
        type: SqlDataType
    ): String? {
        return when (type) {
            SqlDataType.TEXT -> statement.getText(index)
            SqlDataType.INTEGER -> statement.getLong(index).toString()
            SqlDataType.REAL -> statement.getDouble(index).toString()
            else -> statement.getText(index)
        }
    }
}