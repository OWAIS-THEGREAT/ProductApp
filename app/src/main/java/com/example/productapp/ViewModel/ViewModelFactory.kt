package com.example.productapp.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.productapp.Repository.ProductRepository

class ViewModelFactory(private val repository: ProductRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModal(repository) as T
    }
}