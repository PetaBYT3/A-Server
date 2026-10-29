package com.a.server.data.local.platform

interface DirectoryApi {
    fun getApplicationDatabasePath(): String
    fun getServerDatabasePath(): String
}