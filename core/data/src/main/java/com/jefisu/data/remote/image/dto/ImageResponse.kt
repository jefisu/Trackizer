package com.jefisu.data.remote.image.dto

import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    val data: ImageData,
)