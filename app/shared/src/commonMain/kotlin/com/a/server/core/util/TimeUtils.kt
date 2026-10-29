package com.a.server.core.util

fun Long?.millisToDuration(): String {
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