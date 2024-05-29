package com.example.productapp.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.productapp.Modals.productsItem


@Database(entities = [productsItem::class], version = 1, exportSchema = false)
 abstract class ProductDatabase : RoomDatabase() {

    abstract fun getDao():RoomDao

    companion object{
        private var instanse : ProductDatabase? = null

        fun getDatabase(context : Context) : ProductDatabase{
            if(instanse==null){
                instanse = Room.databaseBuilder(
                    context,
                    ProductDatabase::class.java,
                    "ProductDB"
                ).allowMainThreadQueries().build()
            }

            return instanse!!
        }
    }
}