package com.a.server.core.util

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun Long?.toFormattedDuration(): String {
    if (this == null || this <= 0) {
        return "00:00:00, 00 Days"
    }

    val totalSeconds = this / 1000

    val hours = (totalSeconds % 86400) / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    val days = totalSeconds / 86400

    val formattedHours = hours.toString().padStart(2, '0')
    val formattedMinutes = minutes.toString().padStart(2, '0')
    val formattedSeconds = seconds.toString().padStart(2, '0')
    val formattedDays = days.toString().padStart(2, '0')

    return "$formattedHours:$formattedMinutes:$formattedSeconds, $formattedDays Days"
}

fun Long?.toFormattedDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    if (this == null) {
        return "Unknown"
    }

    val localDateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)
    val shortMonths = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    val monthName = shortMonths[localDateTime.monthNumber - 1]
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val year = localDateTime.year

    return "$monthName $day $year"
}