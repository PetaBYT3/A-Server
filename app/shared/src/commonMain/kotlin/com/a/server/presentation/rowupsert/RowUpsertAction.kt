package com.a.server.presentation.rowupsert

sealed interface RowUpsertAction {
    data class CellValue(val columnName: String, val newValue: String): RowUpsertAction
    data object Upsert: RowUpsertAction
}