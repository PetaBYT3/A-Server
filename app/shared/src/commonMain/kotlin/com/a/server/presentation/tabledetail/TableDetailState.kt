package com.a.server.presentation.tabledetail

import com.a.server.domain.model.sqlite.TableCompleteModel

data class TableDetailState(
    val isTableCompleteLoading: Boolean = true,
    val isTableCompleteError: String? = null,
    val tableComplete: TableCompleteModel? = null
)
