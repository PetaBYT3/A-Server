package com.a.server.presentation.database.list

import com.a.server.domain.model.sqlite.TableWithFieldsModel

data class ListState(
    val searchKey: String = "",

    val isTablesLoading: Boolean = true,
    val isTablesError: String? = null,
    val tables: List<TableWithFieldsModel> = emptyList(),
    val filteredTables: List<TableWithFieldsModel> = emptyList()
)
