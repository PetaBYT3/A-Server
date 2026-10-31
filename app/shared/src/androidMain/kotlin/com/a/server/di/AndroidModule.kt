package com.a.server.di

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.a.server.data.local.room.ApplicationDatabase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan(value = ["com.a.server"])
class AndroidModule {
    @Single
    fun provideApplicationDatabase(context: Context): RoomDatabase.Builder<ApplicationDatabase> {
        return Room.databaseBuilder<ApplicationDatabase>(
            context = context.applicationContext,
            name = context.getDatabasePath("application.db").absolutePath,
        )
    }
}