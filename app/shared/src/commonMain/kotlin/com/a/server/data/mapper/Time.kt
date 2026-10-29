package com.a.server.data.mapper

interface Time {
    fun millisToDuration(millis: Long?): String
}