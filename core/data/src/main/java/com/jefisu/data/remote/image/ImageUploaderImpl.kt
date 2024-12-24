package com.jefisu.data.remote.image

import com.jefisu.data.BuildConfig
import com.jefisu.data.remote.image.dto.ImageResponse
import com.jefisu.data.util.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.parameter
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUploaderImpl @Inject constructor(
    private val httpClient: HttpClient,
) : ImageUploader {
    override suspend fun upload(
        filename: String,
        data: ByteArray,
    ): String? {
        return safeCall {
            val response = httpClient.submitFormWithBinaryData(
                url = "https://api.imgbb.com/1/upload",
                formData = formData {
                    append(
                        key = "image",
                        value = data,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "image/*")
                            append(HttpHeaders.ContentDisposition, "filename=$filename")
                        },
                    )
                },
                block = {
                    parameter("key", BuildConfig.IMGBB_KEY)
                },
            )
            response.body<ImageResponse>().data.url
        }
    }
}