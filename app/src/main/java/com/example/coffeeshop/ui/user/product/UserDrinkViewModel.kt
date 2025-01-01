package com.example.coffeeshop.ui.user.product


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.local.CartDao
import com.example.coffeeshop.data.model.CartItem
import com.example.coffeeshop.data.model.Drink
import com.example.coffeeshop.data.model.Topping
import com.example.coffeeshop.data.repository.drink.DrinkRepository
import com.example.coffeeshop.data.repository.topping.ToppingRepositoryImpl
import kotlinx.coroutines.launch


class UserDrinkViewModel(
    private val drinkRepository: DrinkRepository,
    private val toppingRepository: ToppingRepositoryImpl,
    private val cartDao: CartDao
) : ViewModel() {

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int>
        get() = _totalPrice

    private val _drink = MutableLiveData<Drink?>()
    val drink: LiveData<Drink?>
        get() = _drink


    private val _toppings = MutableLiveData<List<Topping>>()
    val toppings: LiveData<List<Topping>>
        get() = _toppings

    private val _itemCount = MutableLiveData<Int>()
    val itemCount: LiveData<Int>
        get() = _itemCount

    private val _selectedTopping = MutableLiveData<List<Topping>>()
    val selectedTopping: LiveData<List<Topping>>
        get() = _selectedTopping

    fun getCartItemById(itemId: String): LiveData<CartItem> {
        return cartDao.getCartItemById(itemId)
    }

    init {
        loadTopping()
    }

    fun addCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            cartDao.insert(cartItem)
        }
    }

    fun updateCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            cartDao.updateCartItem(cartItem)
        }
    }

    fun updateItemCount(count: Int) {
        _itemCount.postValue(count)
    }

    fun updateSelectedTopping(toppings: List<Topping>) {
        _selectedTopping.postValue(toppings)
    }

    fun getDrinkById(drinkId: String) {
        drinkRepository.getDrinkById(drinkId, object : FirebaseCallback<Drink> {
            override fun onSuccess(data: Drink) {
                _drink.postValue(data)
            }

            override fun onFailure(error: String) {
                _drink.postValue(null)
            }
        })
    }

    private fun loadTopping() {
        toppingRepository.loadTopping(object : FirebaseCallback<List<Topping>> {
            override fun onSuccess(data: List<Topping>) {
                _toppings.value = data
            }

            override fun onFailure(message: String) {
                _toppings.value = emptyList()
            }
        })
    }

    fun updateTotalPrice(totalPrice: Int){
        _totalPrice.postValue(totalPrice)
    }


    fun calculateTotalPrice() {
        val drinkValue = drink.value
        Log.d("DrinkValue", drinkValue.toString())
        val price = drinkValue?.price?.toDouble()
            ?: 0.0
        val discount = drinkValue?.discount ?: 0 // Nếu discount là null, giá trị mặc định là 0%

        val actualPrice = price * (100 - discount) / 100
        val count = itemCount.value ?: 1

        val selectedToppingPrice = selectedTopping.value?.sumOf { it.price }?.toDouble() ?: 0.0

        val totalPrice = (actualPrice + selectedToppingPrice) * count

        _totalPrice.value = totalPrice.toInt()

    }


    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: DrinkRepository,
        private val toppingRepository: ToppingRepositoryImpl,
        private val cartDao: CartDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserDrinkViewModel::class.java)) {
                return UserDrinkViewModel(repository, toppingRepository, cartDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

