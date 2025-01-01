package com.example.coffeeshop.ui.admin.drink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Drink
import com.example.coffeeshop.data.repository.drink.DrinkRepositoryImpl

class DrinkViewModel(
    private val repository: DrinkRepositoryImpl
) : ViewModel() {

    private val _drinks = MutableLiveData<List<Drink>>()
    val drinks: LiveData<List<Drink>>
        get() = _drinks

    private val _drinkByCategory = MutableLiveData<List<Drink>>()
    val drinkByCategory: LiveData<List<Drink>>
        get() = _drinkByCategory

    init {
        loadCategory()
    }

    private fun loadCategory() {
        repository.loadDrink(object : FirebaseCallback<List<Drink>> {
            override fun onSuccess(data: List<Drink>) {
                _drinks.postValue(data)
            }

            override fun onFailure(error: String) {
                _drinks.postValue(emptyList())
            }

        })

    }

    fun getDrinkById(drinkId: String, callback: (Drink?) -> Unit) {
        repository.getDrinkById(drinkId, object : FirebaseCallback<Drink> {
            override fun onSuccess(data: Drink) {
                callback(data)
            }

            override fun onFailure(error: String) {
                callback(null)
            }
        })
    }

    fun editDrink(drink: Drink, callback: (Boolean) -> Unit) {
        repository.editDrink(drink) { isSuccess ->
            callback(isSuccess)
        }
    }

    fun loadDrinkByCategory(categoryId: String) {
        repository.getDrinkByCategory(categoryId, object : FirebaseCallback<List<Drink>> {
            override fun onSuccess(data: List<Drink>) {
                _drinkByCategory.postValue(data)
            }

            override fun onFailure(error: String) {
                _drinkByCategory.postValue(emptyList())
            }
        })

    }

    fun addDrink(drink: Drink, callback: (Boolean) -> Unit) {
        repository.addDrink(drink) { isSuccess ->
            callback(isSuccess)
        }
    }

    fun deleteDrink(drink: Drink, callback: (Boolean) -> Unit) {
        repository.deleteDrink(drink) { isSuccess ->
            callback(isSuccess)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: DrinkRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DrinkViewModel::class.java)) {
                return DrinkViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}