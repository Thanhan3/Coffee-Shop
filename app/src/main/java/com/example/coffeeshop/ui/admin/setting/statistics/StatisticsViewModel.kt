package com.example.coffeeshop.ui.admin.setting.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.repository.order.OrderRepositoryImpl

class StatisticsViewModel(
    private val orderRepository: OrderRepositoryImpl
) : ViewModel() {

    private val _order = MutableLiveData<List<Order>>()
    val order: LiveData<List<Order>>
        get() = _order


    fun getOrderByDate(startTime: Long, endTime: Long) {
        orderRepository.getOrdersByTimeRange(startTime, endTime, object :
            FirebaseCallback<List<Order>> {
            override fun onSuccess(data: List<Order>) {
                _order.postValue(data)
            }

            override fun onFailure(error: String) {
                _order.postValue(emptyList())
            }

        })
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: OrderRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
                return StatisticsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}