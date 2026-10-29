package com.a.server.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
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