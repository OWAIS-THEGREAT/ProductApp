package com.example.productapp.Utils

import okio.Buffer
import okio.ForwardingSink
import okio.Sink

class ProgressSink(
    private val sink: Sink,
    private val contentLength: Long,
    private val listener: ProgressListener
) : ForwardingSink(sink) {

    private var bytesWritten = 0L

    override fun write(source: Buffer, byteCount: Long) {
        super.write(source, byteCount)
        bytesWritten += byteCount
        val progress = (bytesWritten.toFloat() / contentLength * 100).toInt()
        listener.onProgress(progress)
    }
}
