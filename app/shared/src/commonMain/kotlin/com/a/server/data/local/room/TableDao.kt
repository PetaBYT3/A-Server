package com.a.server.data.local.room

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDao {
    @Query("SELECT * FROM `table`")
    fun getList(): Flow<List<TableWithFieldsEntity>>

    @Query("SELECT * FROM `table` WHERE name = :name")
    fun getSingle(name: String): Flow<TableWithFieldsEntity?>

    @Upsert
    suspend fun upsert(entity: TableEntity)

    @Query("DELETE FROM `table` WHERE name IN (:names)")
    suspend fun delete(names: List<String>)
}