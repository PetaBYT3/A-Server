package com.a.server.presentation.database.list

sealed interface ListAction {
    data class SearchKey(val newKey: String): ListAction
    data class DeleteTable(val tableName: String): ListAction
}