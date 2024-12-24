package com.jefisu.data.remote.image

interface ImageUploader {
    suspend fun upload(
        filename: String,
        data: ByteArray,
    ): String?
}