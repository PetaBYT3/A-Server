package com.a.server.presentation.database.upsertrow

sealed interface UpsertAction {
    data class CellValue(val columnName: String, val newValue: String): UpsertAction
    data object Upsert: UpsertAction
}