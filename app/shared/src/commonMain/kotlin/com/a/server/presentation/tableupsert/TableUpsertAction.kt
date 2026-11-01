package com.a.server.presentation.tableupsert

import com.a.server.domain.model.sqlite.DataType

sealed interface TableUpsertAction {
    data class TableName(val newName: String): TableUpsertAction
    data class PrimaryKey(val index: Int): TableUpsertAction
    data class FieldName(val index: Int, val newName: String): TableUpsertAction
    data class FieldType(val index: Int, val newType: DataType): TableUpsertAction
    data class RemoveField(val index: Int): TableUpsertAction
    data object AddField: TableUpsertAction

    data object Upsert: TableUpsertAction
}