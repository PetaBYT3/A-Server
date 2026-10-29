@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.a.server.core.manager

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

actual class SystemManager actual constructor() {
    actual fun getInfo(): String {
        val osName = System.getProperty("os.name") ?: "Unknown OS"
        val osVersion = System.getProperty("os.version") ?: "Unknown Version"

        return buildString {
            append(osName)
            append("($osVersion)")
        }
    }

    actual fun copyText(text: String) {
        val selection = StringSelection(text)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, selection)
    }
}