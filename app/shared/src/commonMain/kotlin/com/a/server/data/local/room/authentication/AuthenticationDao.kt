package com.a.server.data.local.room.authentication

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthenticationDao {
    @Query("SELECT * FROM authentication")
    fun getList(): Flow<List<AuthenticationEntity>>

    @Query("SELECT * FROM authentication WHERE id = :authenticationId LIMIT 1")
    fun getSingle(authenticationId: String): Flow<AuthenticationEntity?>

    @Query("SELECT * FROM authentication WHERE email = :email LIMIT 1")
    fun getSingleByEmail(email: String): Flow<AuthenticationEntity?>

    @Query("SELECT * FROM authentication WHERE email = :email AND password = :password LIMIT 1")
    fun getSingleByEmailAndPassword(email: String, password: String): Flow<AuthenticationEntity?>

    @Upsert
    suspend fun upsert(authenticationEntity: AuthenticationEntity)

    @Query("DELETE FROM authentication WHERE id IN (:ids)")
    suspend fun delete(ids: Set<String>)
}