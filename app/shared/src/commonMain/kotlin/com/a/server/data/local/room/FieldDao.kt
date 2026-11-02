package com.a.server.data.local.room

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert

@Dao
interface FieldDao {
    @Upsert
    suspend fun upsert(fieldEntities: List<FieldEntity>)

    @Query("DELETE FROM field WHERE id IN (:ids)")
    suspend fun delete(ids: List<String>)
}