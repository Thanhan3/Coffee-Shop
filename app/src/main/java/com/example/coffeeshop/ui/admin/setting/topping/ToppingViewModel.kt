package com.example.coffeeshop.ui.admin.setting.topping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Topping
import com.example.coffeeshop.data.repository.topping.ToppingRepositoryImpl

class ToppingViewModel(
    private val repository: ToppingRepositoryImpl
) : ViewModel() {

    private val _toppings = MutableLiveData<List<Topping>>()
    val toppings: LiveData<List<Topping>>
        get() = _toppings

    init {
        loadTopping()
    }


    private fun loadTopping() {
        repository.loadTopping(object : FirebaseCallback<List<Topping>> {
            override fun onSuccess(data: List<Topping>) {
                _toppings.value = data
            }

            override fun onFailure(message: String) {
                _toppings.value = emptyList()
            }
        })
    }

    fun addTopping(topping: Topping, callback: (Boolean) -> Unit) {
        repository.addTopping(topping) { isSuccess ->
            callback(isSuccess)
        }
    }

    fun editTopping(topping: Topping, callback: (Boolean) -> Unit) {
        repository.editTopping(topping) { isSuccess ->
            callback(isSuccess)
        }
    }

    fun deleteTopping(topping: Topping, callback: (Boolean) -> Unit) {
        repository.deleteTopping(topping) { isSuccess ->
            callback(isSuccess)
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: ToppingRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ToppingViewModel::class.java)) {
                return ToppingViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}