package com.a.server.presentation.database.scheme

import com.a.server.domain.model.sqlite.TableWithFieldsModel

data class SchemeState(
    val isTablesLoading: Boolean = true,
    val isTablesError: String? = null,
    val tables: List<TableWithFieldsModel> = emptyList()
)
