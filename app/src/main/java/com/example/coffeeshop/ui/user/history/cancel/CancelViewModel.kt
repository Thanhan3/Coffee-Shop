package com.example.coffeeshop.ui.user.history.cancel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.repository.order.OrderRepositoryImpl
import com.example.coffeeshop.ui.user.history.complete.CompleteViewModel

class CancelViewModel(
    private val orderRepositoryImp: OrderRepositoryImpl
) : ViewModel()  {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>>
        get() = _orders

    private val _allOrders = MutableLiveData<List<Order>>()
    val allOrders: LiveData<List<Order>>
        get() = _allOrders


    fun getCancelOrders() {
        orderRepositoryImp.getCancelOrders(object : FirebaseCallback<List<Order>> {
            override fun onSuccess(data: List<Order>) {
                _orders.value = data
            }

            override fun onFailure(message: String) {
                _orders.value = emptyList()
            }
        })
    }

    fun getAllCancelOrders() {
        orderRepositoryImp.getAllCancelOrders(object : FirebaseCallback<List<Order>> {
            override fun onSuccess(data: List<Order>) {
                _allOrders.value = data
            }

            override fun onFailure(message: String) {
                _allOrders.value = emptyList()
            }
        })

    }


    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val orderRepositoryImp: OrderRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CancelViewModel::class.java)) {
                return CancelViewModel(orderRepositoryImp) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}