package com.example.productapp.Utils

import android.os.FileUtils
import okhttp3.Interceptor
import okhttp3.Response


class ProgressInterceptor(private val listener: ProgressListener) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body
        if (requestBody != null) {
            val progressRequestBody = ProgressRequestBody(requestBody, listener)
            val newRequest = request.newBuilder()
                .method(request.method, progressRequestBody)
                .build()
            return chain.proceed(newRequest)
        } else {
            // If the request has no body, proceed without progress tracking
            return chain.proceed(request)
        }
    }
}