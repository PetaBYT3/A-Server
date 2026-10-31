package com.a.server.core.util

import java.io.File
import kotlin.math.pow
import kotlin.math.roundToLong

fun File.calculateSize(): Long {
    if (!this.exists()) return 0L

    if (this.isFile) return this.length()

    var totalSize = 0L
    val children = this.listFiles()

    if (children != null) {
        children.forEach { file ->
            totalSize += if (file.isDirectory) {
                file.calculateSize()
            } else {
                file.length()
            }
        }
    }

    return totalSize
}

fun Long.toReadableSize(decimals: Int = 2): String {
    if (this <= 0) return "0.00 B"

    val bytes = this.toDouble()
    val k = 1024.0
    val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")

    val i = (kotlin.math.log10(bytes) / kotlin.math.log10(k)).toInt().coerceIn(0, units.lastIndex)
    val value = bytes / k.pow(i.toDouble())

    return "${value.format(decimals)} ${units[i]}"
}

private fun Double.format(digits: Int): String {
    var multiplier = 1.0
    repeat(digits) { multiplier *= 10.0 }

    val rounded = (this * multiplier).roundToLong()
    val integerPart = rounded / multiplier.toLong()
    val decimalPart = kotlin.math.abs(rounded % multiplier.toLong())

    val decimalString = decimalPart.toString().padStart(digits, '0')

    return "$integerPart.$decimalString"
}