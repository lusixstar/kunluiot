package com.kunluiot.sdk.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object KotlinSerializationUtils {

    val json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    inline fun <reified T> getJsonData(str: String = ""): T {
        if (str.isNullOrEmpty()) return "" as T
        return json.decodeFromString(str)
    }
}