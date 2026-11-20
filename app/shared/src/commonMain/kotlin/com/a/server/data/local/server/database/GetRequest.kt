package com.a.server.data.local.server.database

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRequest(
    @SerialName("table_name")
    val tableName: String
)
