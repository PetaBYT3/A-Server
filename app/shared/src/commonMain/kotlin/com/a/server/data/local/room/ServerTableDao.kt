package com.a.server.data.local.room

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ServerTableDao {
    @Query("SELECT * FROM server_table_metadata")
    fun getList(): Flow<List<ServerTableEntity>>

    @Upsert
    suspend fun upsert(entity: ServerTableEntity)

    @Delete
    suspend fun delete(entity: ServerTableEntity)
}