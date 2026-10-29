package com.a.server.data.local.platform

import android.content.Context
import org.koin.core.annotation.Single
import java.io.File

@Single
class BinaryManagerApiImpl(
    private val context: Context
): BinaryManagerApi {
    override fun getCloudflarePlatformBinaryFile(): File {
        val binaryFile = File(context.applicationInfo.nativeLibraryDir, "libcloudflared_android_arm64.so")

        return binaryFile
    }
}