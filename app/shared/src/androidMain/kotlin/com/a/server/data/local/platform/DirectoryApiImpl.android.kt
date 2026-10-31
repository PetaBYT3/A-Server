@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.a.server.data.local.platform

import android.content.Context
import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Single
actual class DirectoryApiImpl : DirectoryApi, KoinComponent {
    private val context by inject<Context>()

    override fun getApplicationDatabasePath(): String {
        return context.getDatabasePath("application_database.db").absolutePath
    }

    override fun getServerDatabasePath(): String {
        return context.getDatabasePath("server_database.db").absolutePath
    }

    override fun getStoragePath(): String {
        return context.getExternalFilesDir(null)!!.absolutePath
    }
}