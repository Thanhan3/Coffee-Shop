package com.example.coffeeshop.data.repository.order

import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.OrderDataSource
import com.example.coffeeshop.data.model.Order

class OrderRepositoryImpl(
    private val remoteOrderDataSource: OrderDataSource
):OrderRepository {
    override fun getOderById(id: String, callback: FirebaseCallback<Order>) {
        remoteOrderDataSource.getOderById(id, callback)
    }

    override fun getAllProcessingOrders(callback: FirebaseCallback<List<Order>>) {
        remoteOrderDataSource.getAllProcessingOrders(callback)
    }

    override fun updateStatus(status: Int, order: Order, callback: (Boolean) -> Unit) {
        remoteOrderDataSource.updateStatus(status, order, callback)
    }

    override fun updateSuccess(order: Order, callback: (Boolean) -> Unit) {
        remoteOrderDataSource.updateSuccess(order, callback)
    }

    override fun updateCancel(order: Order, callback: (Boolean) -> Unit) {
        remoteOrderDataSource.updateCancel(order, callback)
    }

    override fun getProcessingOrders(callback: FirebaseCallback<List<Order>>) {
       remoteOrderDataSource.getProcessingOrders(callback)
    }

    override fun getAllCompleteOrders(callback: FirebaseCallback<List<Order>>) {
        remoteOrderDataSource.getAllCompleteOrders(callback)
    }

    override fun getOrdersByTimeRange(
        startTime: Long,
        endTime: Long,
        callback: FirebaseCallback<List<Order>>
    ) {
        remoteOrderDataSource.getOrdersByTimeRange(startTime, endTime, callback)
    }

    override fun getCompleteOrders(callback: FirebaseCallback<List<Order>>) {
        remoteOrderDataSource.getCompleteOrders(callback)
    }

    override fun getCancelOrders(callback: FirebaseCallback<List<Order>>) {
        remoteOrderDataSource.getCancelOrders(callback)
    }

    override fun getAllCancelOrders(callback: FirebaseCallback<List<Order>>) {
        remoteOrderDataSource.getAllCancelOrders(callback)
    }

    override fun editOrder(order: Order, callback: (Boolean) -> Unit) {

    }

    override fun deleteOrder(order: Order, callback: (Boolean) -> Unit) {

    }

    override fun addOrder(order: Order, callback: (Boolean) -> Unit) {

    }
}