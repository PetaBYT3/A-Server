package com.a.server.data.local.platform

import android.content.Context
import org.koin.core.annotation.Single
import java.io.File

@Single
class AndroidDirectoryApiImpl(
    private val context: Context
): DirectoryApi {
    override fun getApplicationDatabasePath(): String {
        val databaseFile = File(context.filesDir, "application_database.db")
        return databaseFile.absolutePath
    }

    override fun getServerDatabasePath(): String {
        val databaseFile = File(context.filesDir, "server_database.db")
        return databaseFile.absolutePath
    }
}