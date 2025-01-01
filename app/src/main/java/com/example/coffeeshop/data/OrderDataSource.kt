package com.example.coffeeshop.data


import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.model.Topping

interface OrderDataSource {
    fun getOrdersByTimeRange(startTime: Long, endTime: Long, callback: FirebaseCallback<List<Order>>)
    fun getOderById(id: String, callback: FirebaseCallback<Order>)
    fun updateStatus(status: Int,order: Order, callback: (Boolean) -> Unit)
    fun updateSuccess(order: Order, callback: (Boolean) -> Unit)
    fun updateCancel(order: Order, callback: (Boolean) -> Unit)
    fun getProcessingOrders(callback: FirebaseCallback<List<Order>>)
    fun getAllProcessingOrders(callback: FirebaseCallback<List<Order>>)
    fun getCompleteOrders(callback: FirebaseCallback<List<Order>>)
    fun getAllCompleteOrders(callback: FirebaseCallback<List<Order>>)
    fun getCancelOrders(callback: FirebaseCallback<List<Order>>)
    fun getAllCancelOrders(callback: FirebaseCallback<List<Order>>)
    fun editOrder(order: Order, callback: (Boolean) -> Unit)
    fun deleteOrder(order: Order, callback: (Boolean) -> Unit)
    fun addOrder(order: Order, callback: (Boolean) -> Unit)
}