package com.example.productapp.Utils

import com.example.productapp.Modals.products
import com.example.productapp.Modals.productsItem
import com.example.productapp.Modals.response_of_add
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface apiInterface {

    @GET("get")
    suspend fun getproducts(
    ): Response<List<productsItem>>

    @POST("add")
    suspend fun addProduct(
        @Body ProductData : MultipartBody
    ): Response<response_of_add>

}