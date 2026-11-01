package com.a.server.presentation.table

import com.a.server.domain.model.sqlite.TableModel

data class TableState(
    val searchKey: String = "",

    val isTableListLoading: Boolean = true,
    val isTableListError: String? = null,
    val tableList: List<TableModel> = emptyList(),
    val filteredTableList: List<TableModel> = emptyList()
)
