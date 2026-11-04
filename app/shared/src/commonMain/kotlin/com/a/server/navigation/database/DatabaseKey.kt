package com.a.server.navigation.database

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface DatabaseKey: NavKey {
    @Serializable
    data object ListScreen: DatabaseKey

    @Serializable
    data class DetailScreen(val tableName: String): DatabaseKey

    @Serializable
    data class UpsertTable(val tableName: String): DatabaseKey

    @Serializable
    data class UpsertRow(val tableName: String, val rowId: String): DatabaseKey
}