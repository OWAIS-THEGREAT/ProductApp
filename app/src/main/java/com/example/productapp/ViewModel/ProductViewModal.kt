package com.example.productapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productapp.Modals.products
import com.example.productapp.Modals.productsItem
import com.example.productapp.Modals.response_of_add
import com.example.productapp.Repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response

class ProductViewModal(private val repository: ProductRepository): ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO){
            repository.getproducatdata()
        }
    }

    suspend fun addproduct(data: MultipartBody) : Response<response_of_add> {
            return repository.addProduct(data)
    }

    val product : LiveData<List<productsItem>>
        get() = repository.productes


}