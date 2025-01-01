package com.example.coffeeshop.ui.user.history.complete

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.repository.order.OrderRepositoryImpl

class CompleteViewModel(
    private val orderRepositoryImp: OrderRepositoryImpl
) : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: MutableLiveData<List<Order>>
        get() = _orders


    private val _allOrders = MutableLiveData<List<Order>>()
    val allOrders: MutableLiveData<List<Order>>
        get() = _allOrders


    init {
        getCompleteOrders()
        getAllCompleteOrders()
    }

    private fun getCompleteOrders() {
        orderRepositoryImp.getCompleteOrders(object : FirebaseCallback<List<Order>> {
            override fun onSuccess(data: List<Order>) {
                _orders.value = data
            }

            override fun onFailure(message: String) {
                _orders.value = emptyList()
            }
        })
    }

    private fun getAllCompleteOrders() {
        orderRepositoryImp.getAllCompleteOrders(object : FirebaseCallback<List<Order>> {
            override fun onSuccess(data: List<Order>) {
                _allOrders.value = data
            }

            override fun onFailure(error: String) {
                _allOrders.value = emptyList()
            }

        })
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val orderRepositoryImp: OrderRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CompleteViewModel::class.java)) {
                return CompleteViewModel(orderRepositoryImp) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}