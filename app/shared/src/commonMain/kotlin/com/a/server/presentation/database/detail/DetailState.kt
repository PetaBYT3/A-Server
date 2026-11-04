package com.a.server.presentation.database.detail

import com.a.server.domain.model.sqlite.TableCompleteModel

data class DetailState(
    val isTableCompleteLoading: Boolean = true,
    val isTableCompleteError: String? = null,
    val tableComplete: TableCompleteModel? = null
)
