package com.example.coffeeshop.data.repository.topping

import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Topping

interface ToppingRepository {
    fun loadTopping(callback: FirebaseCallback<List<Topping>>)
    fun addTopping(topping: Topping, callback: (Boolean) -> Unit)
    fun editTopping(topping: Topping, callback: (Boolean) -> Unit)
    fun deleteTopping(topping: Topping, callback: (Boolean) -> Unit)
}