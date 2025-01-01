package com.example.coffeeshop.ui.user.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.local.CartDao
import com.example.coffeeshop.data.model.Address

import com.example.coffeeshop.data.model.CartItem
import com.example.coffeeshop.data.model.Discount
import com.example.coffeeshop.data.repository.address.AddressRepositoryImpl
import com.example.coffeeshop.data.repository.discount.DiscountRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartDao: CartDao,
    private val discountRepository: DiscountRepositoryImpl,
    private val addressRepository: AddressRepositoryImpl
) : ViewModel() {

    val cartItems: LiveData<List<CartItem>> = cartDao.getAllCartItems()


    private val _discounts = MutableLiveData<List<Discount>>()
    val discounts: LiveData<List<Discount>>
        get() = _discounts

    private val _addresses = MutableLiveData<List<Address>>()
    val addresses: LiveData<List<Address>>
        get() = _addresses

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int>
        get() = _totalPrice


    init {
        cartItems.observeForever { items ->
            val newTotalPrice = items.sumOf { it.totalPrice }
            _totalPrice.value = newTotalPrice
        }
        loadAddresses()
        loadDiscount()
    }

    fun deleteCartItem(cartItem: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.deleteCartItem(cartItem)
        }
    }


    fun deleteAllCartItem(){
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.deleteAllCartItems()
        }
    }

    fun addAddress(address: Address, callback: (Boolean) -> Unit) {
        addressRepository.addAddress(address) { isSuccess ->
            callback(isSuccess)
        }
    }

    fun getAddressDefault(callback: (Address?) -> Unit) {
        addressRepository.getAddressDefault(object : FirebaseCallback<Address> {
            override fun onSuccess(data: Address) {
                callback(data)
            }

            override fun onFailure(error: String) {
                callback(null)
            }
        })
    }

    fun updateAddressDefault(address: Address) {
        addressRepository.editAddress(address) { isSuccess ->
            if (isSuccess) {
            }
        }
    }

    private fun loadAddresses() {
        addressRepository.loadAddress(object : FirebaseCallback<List<Address>> {
            override fun onSuccess(data: List<Address>) {
                _addresses.value = data
            }

            override fun onFailure(error: String) {
                _addresses.value = emptyList()
            }
        })

    }

    private fun loadDiscount() {
        discountRepository.loadDiscount(object : FirebaseCallback<List<Discount>> {
            override fun onSuccess(data: List<Discount>) {
                _discounts.value = data
            }

            override fun onFailure(error: String) {
                _discounts.value = emptyList()
            }
        })
    }

    fun updateCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            cartDao.updateCartItem(cartItem)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val cartDao: CartDao,
        private val discountRepository: DiscountRepositoryImpl,
        private val addressRepository: AddressRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                return CartViewModel(cartDao, discountRepository, addressRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}