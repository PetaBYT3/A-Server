package com.a.server.data.local.room

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [
        AuthenticationEntity::class,
        TableEntity::class,
        FieldEntity::class
    ],
    version = 4,
    exportSchema = false
)
@ColumnTypeConverters(
    value = [
        RoomConverters::class
    ]
)
abstract class ApplicationDatabase: RoomDatabase() {
    abstract val authenticationDao: AuthenticationDao
    abstract val tableDao: TableDao
    abstract val fieldDao: FieldDao
}