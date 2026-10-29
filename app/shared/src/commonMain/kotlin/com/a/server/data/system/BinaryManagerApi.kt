package com.a.server.data.system

import java.io.File

interface BinaryManagerApi {
    fun getCloudflarePlatformBinaryFile(): File
}