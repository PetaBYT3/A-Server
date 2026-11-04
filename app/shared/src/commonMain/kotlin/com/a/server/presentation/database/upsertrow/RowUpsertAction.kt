package com.a.server.presentation.database.upsertrow

sealed interface RowUpsertAction {
    data class CellValue(val columnName: String, val newValue: String): RowUpsertAction
    data object Upsert: RowUpsertAction
}