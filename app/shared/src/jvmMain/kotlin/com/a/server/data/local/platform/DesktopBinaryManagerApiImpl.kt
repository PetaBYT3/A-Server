package com.a.server.data.local.platform

import org.koin.core.annotation.Single
import java.io.File

@Single
class DesktopBinaryManagerApiImpl: BinaryManagerApi {
    override fun getCloudflarePlatformBinaryFile(): File {
        val osName = System.getProperty("os.name").lowercase()
        val binaryFileName = when {
            osName.contains("win") -> "cloudflared_windows_amd64.exe"
            else -> "cloudflared_linux_amd64"
        }

        val tempDir = System.getProperty("java.io.tmpdir")
        val binaryFile = File(tempDir, binaryFileName)

        if (!binaryFile.exists()) {
            javaClass.classLoader?.getResourceAsStream(binaryFileName).use { inputStream ->
                binaryFile.outputStream().use { outputStream ->
                    inputStream?.copyTo(outputStream)
                }
            }
        }

        return binaryFile
    }
}