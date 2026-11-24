package com.a.server.data.local.room.storage

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query("SELECT * FROM `folder`")
    fun getList(): Flow<List<FolderEntity>>

    @Query("SELECT * FROM `folder` WHERE name = :name")
    fun getSingle(name: String): Flow<FolderEntity?>

    @Upsert
    suspend fun upsertList(folders: List<FolderEntity>)

    @Query("DELETE FROM `folder` WHERE name IN (:names)")
    suspend fun deleteList(names: List<String>)
}