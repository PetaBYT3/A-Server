package com.a.server.core.util

import kotlinx.serialization.json.Json

inline fun <reified T> T.toPrettyJson(): String {
    val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
        explicitNulls = false
    }
    return json.encodeToString(this)
}