package com.example.coffeeshop.data


import com.example.coffeeshop.data.model.Topping

interface ToppingDataSource {
    interface Remote {
        fun loadTopping(callback: FirebaseCallback<List<Topping>>)
        fun addTopping(topping: Topping, callback: (Boolean) -> Unit)
        fun editTopping(topping: Topping, callback: (Boolean) -> Unit)
        fun deleteTopping(topping: Topping, callback: (Boolean) -> Unit)
    }
}