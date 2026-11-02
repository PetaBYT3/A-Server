package com.a.server.presentation.tableupsert

import com.a.server.core.type.SqlDataType

sealed interface TableUpsertAction {
    data class TableName(val newName: String): TableUpsertAction
    data class PrimaryKey(val id: String): TableUpsertAction
    data class FieldName(val id: String, val newName: String): TableUpsertAction
    data class FieldType(val id: String, val newType: SqlDataType): TableUpsertAction
    data class RemoveField(val id: String): TableUpsertAction
    data object AddField: TableUpsertAction

    data object Upsert: TableUpsertAction
}