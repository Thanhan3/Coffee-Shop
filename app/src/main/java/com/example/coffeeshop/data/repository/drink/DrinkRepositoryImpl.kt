package com.example.coffeeshop.data.repository.drink

import com.example.coffeeshop.data.DrinkDataSource
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Drink

class DrinkRepositoryImpl(
    private val remote: DrinkDataSource.Remote
): DrinkRepository  {
    override fun loadDrink(callback: FirebaseCallback<List<Drink>>) {
        remote.loadDrink(callback)
    }

    override fun getDrinkByCategory(categoryId: String, callback: FirebaseCallback<List<Drink>>) {
        remote.getDrinkByCategory(categoryId,callback)
    }

    override fun addDrink(drink: Drink, callback: (Boolean) -> Unit) {
        remote.addDrink(drink,callback)
    }

    override fun deleteDrink(drink: Drink, callback: (Boolean) -> Unit) {
        remote.deleteDrink(drink,callback)
    }

    override fun editDrink(drink: Drink, callback: (Boolean) -> Unit) {
        remote.editDrink(drink,callback)
    }

    override fun getDrinkById(drinkId: String, callback: FirebaseCallback<Drink>) {
        remote.getDrinkById(drinkId,callback)
    }

    override fun getOutStandingDrink(callback: FirebaseCallback<List<Drink>>) {
        remote.getOutStandingDrink(callback)
    }
}