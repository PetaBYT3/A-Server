package com.a.server.data.mapper

object TimeMapper: Time {
    override fun millisToDuration(millis: Long?): String {
        if (millis == null || millis <= 0) {
            return "00:00:00, 0 Days"
        }

        val totalSeconds = millis / 1000

        val days = totalSeconds / 86400
        val hours = (totalSeconds % 86400) / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        val formattedHours = hours.toString().padStart(2, '0')
        val formattedMinutes = minutes.toString().padStart(2, '0')
        val formattedSeconds = seconds.toString().padStart(2, '0')

        val dayLabel = if (days == 1L) "Day" else "Days"

        return "$formattedHours:$formattedMinutes:$formattedSeconds, $days $dayLabel"
    }
}