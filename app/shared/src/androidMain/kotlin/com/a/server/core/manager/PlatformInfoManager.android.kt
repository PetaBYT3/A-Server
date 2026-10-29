@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.a.server.core.manager

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

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
}