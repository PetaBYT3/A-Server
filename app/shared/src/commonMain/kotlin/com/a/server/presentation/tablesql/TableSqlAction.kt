package com.a.server.presentation.tablesql

sealed interface TableSqlAction {
    data class Query(val newQuery: String): TableSqlAction
    data object Execute: TableSqlAction
}