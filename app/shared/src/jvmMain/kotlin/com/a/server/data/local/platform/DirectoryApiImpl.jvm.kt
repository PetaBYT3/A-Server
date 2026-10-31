@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.a.server.data.local.platform

import org.koin.core.annotation.Single
import java.io.File

@Single
actual class DirectoryApiImpl : DirectoryApi {
    private val appDirectory = File(System.getProperty("user.home"), ".A Server").apply {
        if (!exists()) mkdirs()
    }
    private val databaseDirectory = File(appDirectory, "Database").apply {
        if (!exists()) mkdirs()
    }

    override fun getApplicationDatabasePath(): String {
        return File(databaseDirectory, "application_database.db").absolutePath
    }

    override fun getServerDatabasePath(): String {
        return File(databaseDirectory, "server_database.db").absolutePath
    }

    override fun getStoragePath(): String {
        return File(appDirectory, "Storage").apply { mkdirs() }.absolutePath
    }
}