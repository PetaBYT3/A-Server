@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@file:SuppressLint("UsableSpace")

package com.a.server.core.manager

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

actual class SystemManager actual constructor(): KoinComponent {
    private val context by inject<Context>()

    actual fun getInfo(): String {
        return "Android (${Build.VERSION.SDK_INT})"
    }

    actual fun copyText(text: String) {
        val clip = ClipData.newPlainText("Copied Text", text)

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(clip)
    }

    actual fun getPathSize(path: String): Long {
        return File(context.filesDir.absolutePath).totalSpace
    }

    actual fun getFreeSize(): Long {
        return File(context.filesDir.absolutePath).usableSpace
    }
}