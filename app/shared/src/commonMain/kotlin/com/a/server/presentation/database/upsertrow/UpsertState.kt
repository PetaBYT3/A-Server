package com.a.server.presentation.database.upsertrow

import com.a.server.domain.model.sqlite.CellModel

data class UpsertState(
    val isOnEdit: Boolean = true,

    val isCellsLoading: Boolean = true,
    val isCellsError: String? = null,
    val cells: List<CellModel> = emptyList(),

    val isDeleteDialogVisible: Boolean = false
)