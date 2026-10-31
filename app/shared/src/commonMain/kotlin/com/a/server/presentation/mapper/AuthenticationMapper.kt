package com.a.server.presentation.mapper

import com.a.server.core.util.toFormattedDate
import com.a.server.domain.model.authentication.AuthenticationModel
import com.a.server.presentation.model.authentication.AuthenticationUi

object AuthenticationMapper: PresentationMapper<AuthenticationModel, AuthenticationUi> {
    override fun toUi(model: AuthenticationModel): AuthenticationUi {
        return AuthenticationUi(
            id = model.id,
            email = model.email,
            password = model.password,
            createdAt = model.createdAt.toFormattedDate()
        )
    }

    override fun toModel(ui: AuthenticationUi): AuthenticationModel {
        return AuthenticationModel(
            id = ui.id,
            email = ui.email,
            password = ui.password,
            createdAt = 0L
        )
    }
}