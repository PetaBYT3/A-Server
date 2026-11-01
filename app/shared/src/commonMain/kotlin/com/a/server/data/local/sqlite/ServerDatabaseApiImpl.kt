package com.a.server.data.local.sqlite

import androidx.sqlite.SQLiteDriver
import androidx.sqlite.SQLiteStatement
import com.a.server.data.local.platform.DirectoryApi
import com.a.server.domain.model.sqlite.FieldModel
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

    override suspend fun upsert(
        tableName: String,
        data: Map<String, Any?>
    ) {
        val columns = data.keys.joinToString(", ")
        val values = data.values.joinToString(", ") { "?" }
        val sql = """
            INSERT OR REPLACE INTO $tableName ($columns) VALUES ($values)
        """.trimIndent()

        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            connection.prepare(sql).use { statement ->
                data.values.forEachIndexed { index, value ->
                    bindValue(statement, index + 1, value)
                }
            }
        }
    }

    override suspend fun delete(tableName: String, id: Long) {
        val sql = "DELETE FROM $tableName WHERE id = ?"

        sqLiteDriver.open(directoryApi.getServerDatabasePath()).use { connection ->
            connection.prepare(sql).use { statement ->
                statement.bindLong(1, id)
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
}