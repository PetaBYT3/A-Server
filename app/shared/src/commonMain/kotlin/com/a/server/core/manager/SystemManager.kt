@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.a.server.core.manager

expect class SystemManager() {
    fun getInfo(): String
    fun copyText(text: String)
}