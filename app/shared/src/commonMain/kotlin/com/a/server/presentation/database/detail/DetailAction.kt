package com.a.server.presentation.database.detail

import com.a.server.domain.model.sqlite.RowModel

sealed interface DetailAction {
    data class SelectRow(val rowModel: RowModel): DetailAction
    data object SelectAllRow: DetailAction
    data object UnselectAllRow: DetailAction
    data object DeleteRowDialog: DetailAction
    data object DeleteRow: DetailAction

    data object DeleteTableDialog: DetailAction
    data object DeleteTable: DetailAction
}