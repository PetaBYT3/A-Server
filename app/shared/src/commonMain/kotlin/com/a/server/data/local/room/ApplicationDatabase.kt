package com.a.server.data.local.room

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.a.server.data.local.room.authentication.AuthenticationDao
import com.a.server.data.local.room.authentication.AuthenticationEntity
import com.a.server.data.local.room.field.FieldDao
import com.a.server.data.local.room.field.FieldEntity
import com.a.server.data.local.room.table.TableDao
import com.a.server.data.local.room.table.TableEntity

@Database(
    entities = [
        AuthenticationEntity::class,
        TableEntity::class,
        FieldEntity::class
    ],
    version = 5,
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