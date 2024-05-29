package com.example.productapp.Utils

import android.util.Log
import android.widget.Toast
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {

    private const val BASE_URL = "https://app.getswipe.in/api/public/"


    val client: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(ProgressInterceptor(object : ProgressListener {
            override fun onProgress(progress: Int) {
                // Update UI with progress
                Log.d("@@@@",progress.toString())
            }
        }))
    }.build()


    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}