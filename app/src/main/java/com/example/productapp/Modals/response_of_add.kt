package com.example.productapp.Modals

data class response_of_add(
    val message: String,
    val product_details: ProductDetails,
    val product_id: Int,
    val success: Boolean
)