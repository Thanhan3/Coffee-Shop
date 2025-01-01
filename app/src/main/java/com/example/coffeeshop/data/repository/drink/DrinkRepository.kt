package com.example.coffeeshop.data.repository.drink

import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Drink

interface DrinkRepository {
    fun loadDrink(callback: FirebaseCallback<List<Drink>>)
    fun getDrinkByCategory(categoryId: String, callback: FirebaseCallback<List<Drink>>)
    fun addDrink(drink: Drink,callback: (Boolean) -> Unit)
    fun deleteDrink(drink: Drink, callback: (Boolean) -> Unit)
    fun editDrink(drink: Drink,callback: (Boolean) -> Unit)
    fun getDrinkById(drinkId: String, callback: FirebaseCallback<Drink>)
    fun getOutStandingDrink(callback: FirebaseCallback<List<Drink>>)
}