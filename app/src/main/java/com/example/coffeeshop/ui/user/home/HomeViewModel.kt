package com.example.coffeeshop.ui.user.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Category
import com.example.coffeeshop.data.model.Drink
import com.example.coffeeshop.data.repository.category.CategoryRepositoryImpl
import com.example.coffeeshop.data.repository.drink.DrinkRepositoryImpl

class HomeViewModel(
    private val drinkRepository: DrinkRepositoryImpl,
    private val categoryRepository: CategoryRepositoryImpl
) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _drinks = MutableLiveData<List<Drink>>()
    val drinks: LiveData<List<Drink>>
        get() = _drinks

    private val _drinkByCategory = MutableLiveData<List<Drink>>()
    val drinkByCategory: LiveData<List<Drink>>
        get() = _drinkByCategory

    private val _outstandingDrinks = MutableLiveData<List<Drink>>()
    val outstandingDrinks: LiveData<List<Drink>>
        get() = _outstandingDrinks

    init {
        loadCategories()
        loadDrinks()
        getOutStandingDrink()
    }

    private fun loadCategories() {
        categoryRepository.loadCategory(object : FirebaseCallback<List<Category>> {
            override fun onSuccess(data: List<Category>) {
                _categories.postValue(data)
            }

            override fun onFailure(error: String) {
                _categories.postValue(emptyList())
            }
        })
    }

    private fun loadDrinks() {
        drinkRepository.loadDrink(object : FirebaseCallback<List<Drink>> {
            override fun onSuccess(data: List<Drink>) {
                _drinks.postValue(data)
            }

            override fun onFailure(error: String) {
                _drinks.postValue(emptyList())
            }
        })
    }

    fun getDrinkByCategory(categoryId: String) {
        drinkRepository.getDrinkByCategory(categoryId, object : FirebaseCallback<List<Drink>> {
            override fun onSuccess(data: List<Drink>) {
                _drinkByCategory.postValue(data)
            }

            override fun onFailure(error: String) {
                _drinkByCategory.postValue(emptyList())
            }
        })

    }

    private fun getOutStandingDrink() {
        drinkRepository.getOutStandingDrink(object : FirebaseCallback<List<Drink>> {
            override fun onSuccess(data: List<Drink>) {
                _outstandingDrinks.postValue(data)
            }

            override fun onFailure(error: String) {
                _outstandingDrinks.postValue(emptyList())
            }
        })
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: DrinkRepositoryImpl,
        private val categoryRepository: CategoryRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(repository, categoryRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}