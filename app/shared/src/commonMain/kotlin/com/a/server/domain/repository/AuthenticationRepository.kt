package com.a.server.domain.repository

import arrow.core.Either
import com.a.server.domain.model.authentication.AuthenticationModel
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun getList(): Flow<Either<String, List<AuthenticationModel>>>
    fun getSingle(authenticationId: String): Flow<Either<String, AuthenticationModel>>
    fun delete(ids: Set<String>): Flow<Either<String, String>>
}