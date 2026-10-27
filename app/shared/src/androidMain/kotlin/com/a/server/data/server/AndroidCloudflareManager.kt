package com.a.server.data.server

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single
import java.io.File
import java.util.Scanner

@Single
class AndroidCloudflareManager(
    private val context: Context
): CloudflareManager {
    private val _logs = MutableStateFlow<List<String>>(emptyList())
    override val logs: StateFlow<List<String>> = _logs.asStateFlow()

    private var process: Process? = null

    override fun start(port: Int) {
        if (process != null) {
            return
        }

        val binaryFile = File(context.applicationInfo.nativeLibraryDir, "libcloudflared.so")

        process = ProcessBuilder(
            binaryFile.absolutePath,
            "tunnel",
            "--url",
            "http://localhost:$port"
        ).redirectErrorStream(true).start()

        Thread {
            val scanner = Scanner(process!!.inputStream)

            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()

                _logs.update { currentLog ->
                    currentLog + line
                }
            }
        }.start()
    }

    override fun stop() {
        process?.destroy()
        process = null
    }
}