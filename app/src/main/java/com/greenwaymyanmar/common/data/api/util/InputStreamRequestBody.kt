package com.greenwaymyanmar.common.data.api.util

import android.content.ContentResolver
import android.net.Uri
import greenway_myanmar.org.util.extensions.fileName
import greenway_myanmar.org.util.extensions.toPlainTextRequestBody
import greenway_myanmar.org.vo.ServerImageContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okio.*
import java.util.*
import kotlin.io.use

class InputStreamRequestBody
constructor(
    private val contentType: MediaType,
    private val contentResolver: ContentResolver,
    private val uri: Uri
) : RequestBody() {

    override fun contentType(): MediaType = contentType

    @Throws(IOException::class)
    override fun contentLength(): Long = -1

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val input = contentResolver.openInputStream(uri)

        input?.use { sink.writeAll(it.source()) } ?: throw IOException("Could not open $uri")
    }
}

fun buildImageRequest(
    contentResolver: ContentResolver,
    serverImageContext: ServerImageContext,
    uri: Uri
): Map<String, RequestBody> {

    val map = mutableMapOf<String, RequestBody>()

    map["for"] = serverImageContext.value.toPlainTextRequestBody()

    val filename: String = uri.fileName(contentResolver).ifEmpty { UUID.randomUUID().toString() }
    map["file\"; filename=\"$filename"] =
        InputStreamRequestBody("image/*".toMediaType(), contentResolver, uri)

    return map
}
