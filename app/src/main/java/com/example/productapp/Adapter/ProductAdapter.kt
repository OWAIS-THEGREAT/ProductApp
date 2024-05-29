package com.example.productapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productapp.Modals.products
import com.example.productapp.Modals.productsItem
import com.example.productapp.Product_Data
import com.example.productapp.R
import java.util.Locale

class ProductAdapter(val context:Context, private var itemList: MutableList<productsItem>):RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    private var filteredItemList: MutableList<productsItem>  = itemList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val Productname  = itemView.findViewById<TextView>(R.id.textView)
        val productImage = itemView.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.Productname.text = filteredItemList[position].product_name
        Glide.with(context)
            .load(filteredItemList[position].image)
            .placeholder(R.drawable.img)
            .into(holder.productImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context,Product_Data::class.java)

            intent.putExtra("name",filteredItemList[position].product_name)
            intent.putExtra("price",filteredItemList[position].price.toString())
            intent.putExtra("tax",filteredItemList[position].tax.toString())
            intent.putExtra("type",filteredItemList[position].product_type)
            intent.putExtra("image",filteredItemList[position].image)
            context.startActivity(intent)
        }
    }

    fun filter(query: String) {
        filteredItemList = if (query.isEmpty()) {
            itemList
        } else {
            val lowerCaseQuery = query.lowercase(Locale.getDefault())
            itemList.filter {
                it.product_name.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun updateList(newList: List<productsItem>) {
        itemList = newList.toMutableList()
        filteredItemList = newList.toMutableList()
        notifyDataSetChanged()
    }

}