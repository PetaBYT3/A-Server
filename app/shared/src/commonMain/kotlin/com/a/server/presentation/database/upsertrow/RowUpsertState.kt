package com.a.server.presentation.database.upsertrow

import com.a.server.domain.model.sqlite.CellModel

data class RowUpsertState(
    val isCellsLoading: Boolean = true,
    val isCellsError: String? = null,
    val cells: List<CellModel> = emptyList()
)