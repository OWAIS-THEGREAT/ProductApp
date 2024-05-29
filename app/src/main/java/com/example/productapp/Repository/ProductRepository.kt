package com.example.productapp.Repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.productapp.Modals.productsItem
import com.example.productapp.Modals.response_of_add
import com.example.productapp.Room.ProductDatabase
import com.example.productapp.Utils.InternetCheck
import com.example.productapp.Utils.apiInterface
import okhttp3.MultipartBody
import retrofit2.Response

class ProductRepository(
    private val apiInterface: apiInterface,
    private val productDatabase: ProductDatabase,
    private val applicationContext: Context
) {

    private val productLiveData = MutableLiveData<List<productsItem>>()

    val productes : LiveData<List<productsItem>>
        get() = productLiveData


    suspend fun getproducatdata(){

        if(InternetCheck.isInternetAvailable((applicationContext))){
            val result = apiInterface.getproducts()
            if(result.body()!=null){

                productDatabase.getDao().insertProducts(result.body()!!)
                productLiveData.postValue(result.body())
            }

        }else{

            val products = productDatabase.getDao().getlist()
            productLiveData.postValue(products)
        }

    }

    suspend fun addProduct(data : MultipartBody) : Response<response_of_add>{
        val result = apiInterface.addProduct(data)
        return result
    }

}