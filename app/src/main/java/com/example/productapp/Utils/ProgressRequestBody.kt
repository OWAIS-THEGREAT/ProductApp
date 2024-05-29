package com.example.productapp.Utils

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Okio
import okio.buffer

class ProgressRequestBody(
    private val requestBody: RequestBody,
    private val listener: ProgressListener
) : RequestBody() {

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    override fun writeTo(sink: BufferedSink) {
        val bufferedSink = ProgressSink(sink, contentLength(), listener)
        val bufferedSinkProgress = bufferedSink.buffer()
        requestBody.writeTo(bufferedSinkProgress)
        bufferedSinkProgress.flush()
    }
}
