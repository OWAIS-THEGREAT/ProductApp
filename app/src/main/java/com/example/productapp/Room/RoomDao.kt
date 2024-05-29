package com.example.productapp.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.productapp.Modals.productsItem

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products : List<productsItem>)

    @Query("SELECT * FROM `Product Items`")
    suspend fun getlist() : List<productsItem>
}