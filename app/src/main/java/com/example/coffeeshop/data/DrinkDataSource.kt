package com.example.coffeeshop.data

import com.example.coffeeshop.data.model.Drink

interface DrinkDataSource {
    interface Remote{
        fun loadDrink(callback: FirebaseCallback<List<Drink>>)
        fun addDrink(drink: Drink,callback: (Boolean) -> Unit)
        fun deleteDrink(drink: Drink,callback: (Boolean) -> Unit)
        fun getDrinkByCategory(categoryId: String, callback: FirebaseCallback<List<Drink>>)
        fun editDrink(drink: Drink,callback: (Boolean) -> Unit)
        fun getDrinkById(drinkId: String, callback: FirebaseCallback<Drink>)
        fun getOutStandingDrink(callback: FirebaseCallback<List<Drink>>)
    }
}