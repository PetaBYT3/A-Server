package com.a.server.data.local.server.database

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class GetResponse(
    val success: Boolean,
    val message: String,
    val data: List<JsonObject> = emptyList()
) {
    inline fun <reified T> decodeList(
        json: Json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    ): List<T> {
        return data.map { jsonObject ->
            json.decodeFromJsonElement<T>(jsonObject)
        }
    }
}
