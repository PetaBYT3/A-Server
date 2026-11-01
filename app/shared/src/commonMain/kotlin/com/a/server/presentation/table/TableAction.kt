package com.a.server.presentation.table

sealed interface TableAction {
    data class SearchKey(val newKey: String): TableAction
    data class DeleteTable(val tableName: String): TableAction
}