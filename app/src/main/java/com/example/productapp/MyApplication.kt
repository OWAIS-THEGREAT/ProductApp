package com.example.productapp

import android.app.Application
import com.example.productapp.Repository.ProductRepository
import com.example.productapp.Room.ProductDatabase
import com.example.productapp.Utils.ProgressListener
import com.example.productapp.Utils.RetrofitObject
import com.example.productapp.Utils.apiInterface

class MyApplication : Application() {

    lateinit var productRepository: ProductRepository
    override fun onCreate() {
        super.onCreate()

        val apiInterface = RetrofitObject.instance.create(apiInterface::class.java)
        val database = ProductDatabase.getDatabase(applicationContext)
        productRepository = ProductRepository(apiInterface,database,applicationContext)

    }
}