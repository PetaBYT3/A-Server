package com.a.server.data.server

import com.a.server.data.util.urlRegex
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single
import java.io.File
import java.util.Scanner

@Single
class DesktopCloudflareManager: CloudflareManager {
    private val _publicUrl = MutableStateFlow<String?>(null)
    override val publicUrl: StateFlow<String?> = _publicUrl

    private var process: Process? = null

    override fun start(port: Int) {
        if (process != null) {
            return
        }

        val osName = System.getProperty("os.name").lowercase()
        val binaryName = when {
            osName.contains("win") -> "cloudflared-windows-amd64.exe"
            else -> "cloudflared-linux-amd64"
        }

        val tempDir = System.getProperty("java.io.tmpdir")
        val binaryFile = File(tempDir, binaryName)

        binaryFile.setExecutable(true, false)

        if (!binaryFile.exists()) {
            javaClass.classLoader?.getResourceAsStream(binaryName).use { inputStream ->
                binaryFile.outputStream().use { outputStream ->
                    inputStream?.copyTo(outputStream)
                }
            }
        }

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
                val matchResult = urlRegex.find(line)

                if (matchResult != null) {
                    _publicUrl.update { matchResult.value }
                }
            }
        }.start()
    }

    override fun stop() {
        process?.destroy()
        process = null
    }
}