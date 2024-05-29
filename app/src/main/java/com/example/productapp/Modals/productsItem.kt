package com.example.productapp.Modals

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Product Items")
data class productsItem(
    val image: String,
    val price: Double,
    @PrimaryKey
    val product_name: String,
    val product_type: String,
    val tax: Double
)