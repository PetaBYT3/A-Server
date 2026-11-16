package com.a.server.presentation.database.executesql

sealed interface ExecuteSqlAction {
    data class SqlCommand(val newCommand: String): ExecuteSqlAction
    data object Execute: ExecuteSqlAction
}