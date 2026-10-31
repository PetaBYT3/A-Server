package com.a.server.domain.repository

import com.a.server.domain.model.storage.StorageModel
import kotlinx.coroutines.flow.StateFlow

interface StorageRepository {
    val state: StateFlow<StorageModel>
}