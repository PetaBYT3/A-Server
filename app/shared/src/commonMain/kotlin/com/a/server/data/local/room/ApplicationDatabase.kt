package com.a.server.data.local.room

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [
        ServerTableEntity::class,
        AuthenticationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {
    abstract val serverTableDao: ServerTableDao
    abstract val authenticationDao: AuthenticationDao
}