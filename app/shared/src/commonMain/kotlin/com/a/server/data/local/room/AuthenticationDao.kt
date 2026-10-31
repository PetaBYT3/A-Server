package com.a.server.data.local.room

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthenticationDao {
    @Query("SELECT * FROM authentication")
    fun getList(): Flow<List<AuthenticationEntity>>

    @Query("SELECT * FROM authentication WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getSingleByEmailAndPassword(email: String, password: String): AuthenticationEntity?

    @Upsert
    suspend fun upsert(authenticationEntity: AuthenticationEntity)

    @Query("DELETE FROM authentication WHERE id IN (:ids)")
    suspend fun delete(ids: Set<String>)
}