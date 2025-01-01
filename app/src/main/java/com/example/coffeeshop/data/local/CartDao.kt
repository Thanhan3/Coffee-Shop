package com.example.coffeeshop.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.coffeeshop.data.model.CartItem


@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cart: CartItem)

    @Query("SELECT * FROM cart")
    fun getAllCartItems(): LiveData<List<CartItem>>

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("SELECT * FROM cart WHERE id = :itemId")
    fun getCartItemById(itemId: String): LiveData<CartItem>

    @Query("DELETE FROM cart")
    fun deleteAllCartItems()
}