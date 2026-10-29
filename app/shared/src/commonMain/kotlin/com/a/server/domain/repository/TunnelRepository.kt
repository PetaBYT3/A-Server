package com.a.server.domain.repository

import arrow.core.Either
import com.a.server.domain.model.tunnel.TunnelModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TunnelRepository {
    val tunnelState: StateFlow<TunnelModel>

    fun toggle(): Flow<Either<String, String>>
}