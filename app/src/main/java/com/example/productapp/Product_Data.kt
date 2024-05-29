package com.example.productapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class Product_Data : AppCompatActivity() {
    private lateinit var image_prod : ImageView
    private lateinit var name_prod : TextView
    private lateinit var type_prod : TextView
    private lateinit var tax_prod : TextView
    private lateinit var price_prod : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_data)
        val name = intent.getStringExtra("name")
        val price = intent.getStringExtra("price")
        val tax = intent.getStringExtra("tax")
        val type = intent.getStringExtra("type")
        val image = intent.getStringExtra("image")

        image_prod = findViewById(R.id.image_prod)
        name_prod = findViewById(R.id.name_prod)
        type_prod = findViewById(R.id.type_prod)
        price_prod = findViewById(R.id.price_prod)
        tax_prod = findViewById(R.id.tax_prod)

        name_prod.text = "Product Name : " + name
        type_prod.text = "Product Type : " + type
        price_prod.text = "Product Price(Rs) : " + price
        tax_prod.text = "Product Tax(%) : " + tax

        Glide.with(this).load(image).placeholder(R.drawable.img).into(image_prod)

    }
}