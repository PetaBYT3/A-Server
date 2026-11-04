package com.a.server.presentation.database.upserttable

import com.a.server.core.type.SqlDataType

sealed interface UpsertAction {
    data class TableName(val newName: String): UpsertAction
    data class PrimaryKey(val id: String): UpsertAction
    data class FieldName(val id: String, val newName: String): UpsertAction
    data class FieldType(val id: String, val newType: SqlDataType): UpsertAction
    data class RemoveField(val id: String): UpsertAction
    data object AddField: UpsertAction

    data object Upsert: UpsertAction
}