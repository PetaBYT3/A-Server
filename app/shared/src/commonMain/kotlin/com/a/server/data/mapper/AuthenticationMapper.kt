package com.a.server.data.mapper

import com.a.server.data.local.room.AuthenticationEntity
import com.a.server.domain.model.authentication.AuthenticationModel

object AuthenticationMapper: DataMapper<AuthenticationEntity, AuthenticationModel> {
    override fun toModel(dto: AuthenticationEntity): AuthenticationModel {
        return AuthenticationModel(
            id = dto.id,
            email = dto.email,
            password = dto.password,
            createdAt = dto.createdAt,
        )
    }

    override fun toDto(model: AuthenticationModel): AuthenticationEntity {
        return AuthenticationEntity(
            id = model.id,
            email = model.email,
            password = model.password,
            createdAt = model.createdAt,
        )
    }
}