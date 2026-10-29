package com.a.server.domain.repository

import arrow.core.Either
import com.a.server.domain.model.server.ServerModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ServerRepository {
    val serverState: StateFlow<ServerModel>

    fun toggle(): Flow<Either<String, String>>
}