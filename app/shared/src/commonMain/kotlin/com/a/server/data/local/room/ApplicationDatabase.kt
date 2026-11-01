package com.a.server.data.local.room

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [
        TableEntity::class,
        AuthenticationEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {
    abstract val tableDao: TableDao
    abstract val authenticationDao: AuthenticationDao
}