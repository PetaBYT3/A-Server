package com.a.server.data.local.room

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDao {
    @Query("SELECT * FROM server_table_metadata")
    fun getList(): Flow<List<TableEntity>>

    @Upsert
    suspend fun upsert(entity: TableEntity)

    @Query("DELETE FROM server_table_metadata WHERE table_name IN (:tableNames)")
    suspend fun delete(tableNames: List<String>)
}