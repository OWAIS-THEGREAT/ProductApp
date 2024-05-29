package com.example.productapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productapp.Adapter.ProductAdapter
import com.example.productapp.Modals.productsItem
import com.example.productapp.Utils.ProgressListener
import com.example.productapp.Utils.RetrofitObject
import com.example.productapp.ViewModel.ProductViewModal
import com.example.productapp.ViewModel.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(){

    private lateinit var productViewModal: ProductViewModal
    private lateinit var recyclerView : RecyclerView
    private lateinit var productList : MutableList<productsItem>
    private lateinit var searchView : SearchView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var addButton : FloatingActionButton
    private lateinit var product_name : EditText
    private lateinit var product_type : Spinner
    private lateinit var product_tax : EditText
    private lateinit var imagepicker : ImageView
    private lateinit var product_price : EditText
    private lateinit var progressBar  : ProgressBar
    private lateinit var submit : Button
    private var imageFile : File ?= null

    private val IMAGE_PICK_CODE = 1000
    private var selectedImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerview)
        addButton = findViewById(R.id.addProduct)
        searchView = findViewById(R.id.searchView)
        progressBar = findViewById(R.id.progressBar)
        productList = mutableListOf()

        val repository = (application as MyApplication).productRepository

        productViewModal = ViewModelProvider(this, ViewModelFactory(repository))[ProductViewModal::class.java]

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        productAdapter = ProductAdapter(this, productList)


        recyclerView.adapter = productAdapter

        lifecycleScope.launch(Dispatchers.IO) {
            val productData = productViewModal.product
            withContext(Dispatchers.Main) {
                productData.observe(this@MainActivity) { data ->
                    productList.clear()
                    productList.addAll(data)
                    productAdapter.updateList(productList)
                }
            }
        }
        searchView.setOnClickListener {
            searchView.isIconified = false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    productAdapter.filter(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    productAdapter.filter(it)
                }
                return true
            }
        })

        addButton.setOnClickListener {
            showBottomSheetDialog()
        }

    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        // Inflate the layout for the Bottom Sheet
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        product_name = bottomSheetView.findViewById(R.id.productName)
        product_tax  = bottomSheetView.findViewById(R.id.ProductTax)
        imagepicker = bottomSheetView.findViewById(R.id.productImage)
        submit = bottomSheetView.findViewById(R.id.submit)
        product_type = bottomSheetView.findViewById(R.id.ProductType)
        product_price = bottomSheetView.findViewById(R.id.ProductPrice)

        val productTypes = arrayOf("Electronics", "Clothing", "Food", "Books")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        product_type.adapter = adapter

        imagepicker.setOnClickListener {
            pickImageFromGallery()
        }

        bottomSheetDialog.setOnShowListener {
            bottomSheetDialog.window?.setWindowAnimations(R.style.DialogAnimation)
        }

        submit.setOnClickListener {
            val name = product_name.text.toString()
            val type = product_type.selectedItem.toString()
            val tax = product_tax.text.toString()
            val file = imageFile
            val price = product_price.text.toString()
            if(name.trim().isNotEmpty() && type.isNotEmpty() && tax.trim().isNotEmpty() && price.trim().isNotEmpty()){

                val multipartBodyBuilder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("product_name", name)
                    .addFormDataPart("product_type", type)
                    .addFormDataPart("price", price)
                    .addFormDataPart("tax", tax)

                imageFile?.let {
                    val requestBody = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("files[]", it.name, requestBody)
                    multipartBodyBuilder.addPart(imagePart)
                }

                val multipartBody = multipartBodyBuilder.build()

                lifecycleScope.launch(Dispatchers.IO) {
                    val result = productViewModal.addproduct(multipartBody)
                    withContext(Dispatchers.Main) {
                        if(result.body()!!.success){
                            showSuccessDialog()
                        }
                    }
                }

            }
            else{
                Toast.makeText(this@MainActivity,"Enter Details",Toast.LENGTH_SHORT).show()
            }
        }
        bottomSheetDialog.show()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            selectedImageUri = data?.data

            imagepicker.setImageURI(selectedImageUri)
            imageFile = getImageFileFromUri(selectedImageUri!!)
        }
    }

    private fun getImageFileFromUri(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "temp_image.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
        builder.setMessage("Product added successfully")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }


}