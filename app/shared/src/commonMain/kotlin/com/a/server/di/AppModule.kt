package com.a.server.di

import androidx.room.Room
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.a.server.data.local.room.ApplicationDatabase
import com.a.server.data.local.platform.DirectoryApi
import com.a.server.data.local.room.ServerTableDao
import com.russhwolf.settings.ObservableSettings
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import com.russhwolf.settings.Settings
import kotlinx.coroutines.Dispatchers


@Module
@ComponentScan(value = ["com.a.server"])
class AppModule {
    @Single
    fun provideSettings(): ObservableSettings {
        return Settings() as ObservableSettings
    }

    @Single
    fun provideApplicationDatabase(directoryApi: DirectoryApi): ApplicationDatabase {
        return Room.databaseBuilder<ApplicationDatabase>(directoryApi.getApplicationDatabasePath())
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    @Single
    fun provideServerTableDao(database: ApplicationDatabase): ServerTableDao {
        return database.serverTableDao
    }

    @Single
    fun provideServerDatabase(): SQLiteDriver {
        return BundledSQLiteDriver()
    }

    @Single
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}