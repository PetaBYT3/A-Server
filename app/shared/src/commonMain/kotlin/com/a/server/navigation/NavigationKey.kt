package com.a.server.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationKey: NavKey {
    //Dashboard
    @Serializable
    data object DashboardMain: NavigationKey


    //Authentication
    @Serializable
    data object AuthenticationList: NavigationKey

    @Serializable
    data class AuthenticationDetail(val authId: String): NavigationKey

    @Serializable
    data object AuthenticationDocs: NavigationKey


    //Database
    @Serializable
    data object DatabaseList: NavigationKey

    @Serializable
    data class DatabaseDetail(val tableName: String): NavigationKey

    @Serializable
    data class DatabaseUpsertTable(val tableName: String): NavigationKey

    @Serializable
    data class DatabaseUpsertRow(val tableName: String, val rowId: String): NavigationKey

    @Serializable
    data object DatabaseScheme: NavigationKey

    @Serializable
    data object DatabaseExecuteSql: NavigationKey


    //Storage
    @Serializable
    data object StorageList: NavigationKey

    @Serializable
    data class StorageDetail(val folderName: String): NavigationKey
}