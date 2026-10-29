package com.a.server.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ServerTableEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {
    abstract val serverTableDao: ServerTableDao
}