package com.example.coffeeshop.data.repository.topping

import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.ToppingDataSource
import com.example.coffeeshop.data.model.Topping

class ToppingRepositoryImpl(
    private val remote: ToppingDataSource.Remote
): ToppingRepository {
    override fun loadTopping(callback: FirebaseCallback<List<Topping>>) {
        remote.loadTopping(callback)
    }

    override fun addTopping(topping: Topping, callback: (Boolean) -> Unit) {
        remote.addTopping(topping, callback)
    }

    override fun editTopping(topping: Topping, callback: (Boolean) -> Unit) {
        remote.editTopping(topping, callback)
    }

    override fun deleteTopping(topping: Topping, callback: (Boolean) -> Unit) {
        remote.deleteTopping(topping, callback)
    }

}