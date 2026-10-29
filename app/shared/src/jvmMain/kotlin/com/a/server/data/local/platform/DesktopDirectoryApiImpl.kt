package com.a.server.data.local.platform

import org.koin.core.annotation.Single
import java.io.File

@Single
class DesktopDirectoryApiImpl: DirectoryApi {
    override fun getApplicationDatabasePath(): String {
        val appDirectory = File(System.getProperty("user.home"), ".A Server").apply {
            if (!exists()) mkdirs()
        }

        val databaseFile = File(appDirectory, "application_database.db")
        return databaseFile.absolutePath
    }

    override fun getServerDatabasePath(): String {
        val appDirectory = File(System.getProperty("user.home"), ".A Server").apply {
            if (!exists()) mkdirs()
        }

        val databaseFile = File(appDirectory, "server_database.db")
        return databaseFile.absolutePath
    }
}