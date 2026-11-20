package com.a.server.presentation.database.scheme

import com.a.server.domain.model.sqlite.TableWithFieldsModel

sealed interface SchemeAction {
    data class OnDragComplete(val tableWithField: TableWithFieldsModel): SchemeAction
}