package com.a.server.domain.repository

import arrow.core.Either
import com.a.server.domain.model.TunnelModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TunnelRepository {
    val state: StateFlow<TunnelModel>

    fun start(): Flow<Either<String, String>>
    fun stop()
}