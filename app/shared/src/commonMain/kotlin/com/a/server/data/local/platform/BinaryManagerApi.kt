package com.a.server.data.local.platform

import java.io.File

interface BinaryManagerApi {
    fun getCloudflarePlatformBinaryFile(): File
}