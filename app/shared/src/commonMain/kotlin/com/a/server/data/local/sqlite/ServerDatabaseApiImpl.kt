package com.a.server.data.local.sqlite

import androidx.sqlite.SQLiteDriver
import androidx.sqlite.SQLiteStatement
import com.a.server.core.type.DataType
import com.a.server.data.local.platform.DirectoryApi
import com.a.server.domain.model.sqlite.CellModel
import com.a.server.domain.model.sqlite.FieldModel
import com.a.server.domain.model.sqlite.RowModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
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

    override suspend fun deleteTable(names: List<String>) {
        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            names.forEach { tableName ->
                val sql = "DROP TABLE IF EXISTS `$tableName`"
                connection.prepare(sql).use { statement ->
                    statement.step()
                }
            }
        }
    }

    override suspend fun getRows(tableWithFields: TableWithFieldsModel): List<RowModel> {
        val columnNames = tableWithFields.fields.joinToString(", ") { "`${it.name}`" }
        val sql = "SELECT $columnNames FROM `${tableWithFields.table.name}`"
        val rowList = mutableListOf<RowModel>()

        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            connection.prepare(sql).use { statement ->
                while (statement.step()) {
                    val cellList = mutableListOf<CellModel>()

                    tableWithFields.fields.forEachIndexed { index, field ->
                        val cell = CellModel(
                            isPrimary = field.primary,
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
                            isPrimary = field.primary,
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

    override suspend fun deleteRow(tableName: String, columnName: String, rowIds: List<String>) {
        if (rowIds.isEmpty()) return

        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            val sql = "DELETE FROM `$tableName` WHERE `$columnName` = ?"

            connection.prepare(sql).use { statement ->
                rowIds.forEach { rowId ->
                    statement.bindText(1, rowId)
                    statement.step()
                    statement.reset()
                }
            }
        }
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
                        DataType.INTEGER -> {
                            val longVal = cell.value?.toLongOrNull()
                            if (longVal != null) {
                                statement.bindLong(bindIndex, longVal)
                            } else {
                                statement.bindNull(bindIndex)
                            }
                        }
                        DataType.REAL -> {
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

    override suspend fun getDynamicRows(tableName: String): List<JsonObject> {
        require(tableName.matches(Regex("^[a-zA-Z_][a-zA-Z0-9_]*$"))) {
            "Table unavailable"
        }

        val sql = "SELECT * FROM `$tableName`"
        val resultList = mutableListOf<JsonObject>()

        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            connection.prepare(sql).use { statement ->
                val columnCount = statement.getColumnCount()
                val columnNames = (0 until columnCount).map { index ->
                    statement.getColumnName(index)
                }

                while (statement.step()) {
                    val rowMap = mutableMapOf<String, JsonElement>()

                    for (index in 0 until columnCount) {
                        val columnName = columnNames[index]
                        rowMap[columnName] = statement.getJsonElement(index)
                    }
                    resultList.add(JsonObject(rowMap))
                }
            }
        }

        return resultList
    }

    private fun SQLiteStatement.getJsonElement(index: Int): JsonElement {
        if (isNull(index)) return JsonNull

        return when (getColumnType(index)) {
            1 -> JsonPrimitive(getLong(index))
            2 -> JsonPrimitive(getDouble(index))
            3 -> JsonPrimitive(getText(index))
            4 -> JsonPrimitive(getBlob(index).decodeToString())
            else -> JsonNull
        }
    }

    private fun getValueAsString(
        statement: SQLiteStatement,
        index: Int,
        type: DataType
    ): String? {
        return when (type) {
            DataType.TEXT -> statement.getText(index)
            DataType.INTEGER -> statement.getLong(index).toString()
            DataType.REAL -> statement.getDouble(index).toString()
            else -> statement.getText(index)
        }
    }
}