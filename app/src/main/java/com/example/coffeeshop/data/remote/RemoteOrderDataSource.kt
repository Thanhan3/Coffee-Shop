package com.example.coffeeshop.data.remote

import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.OrderDataSource
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.utils.Constant.CANCEL
import com.example.coffeeshop.utils.Constant.PROCESSING
import com.example.coffeeshop.utils.Constant.SUCCESS
import com.example.coffeeshop.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RemoteOrderDataSource : OrderDataSource {
    override fun getOderById(id: String, callback: FirebaseCallback<Order>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Orders")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (user in snapshot.children) {
                    for (item in user.children) {
                        val order = item.getValue(Order::class.java)
                        if (order != null) {
                            if (order.orderId == id) {
                                callback.onSuccess(order)
                                return
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }

        })
    }

    override fun updateStatus(status: Int, order: Order, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance().reference.child("Orders")
            .child(order.userId).child(order.orderId)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ref.child("shopStatus").setValue(status)
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }

        })
    }

    override fun updateSuccess(order: Order, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance().reference.child("Orders")
            .child(order.userId).child(order.orderId)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ref.child("status").setValue(SUCCESS)
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }

        })
    }

    override fun updateCancel(order: Order, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance().reference.child("Orders")
            .child(order.userId).child(order.orderId)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ref.child("status").setValue(CANCEL)
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }

        })
    }

    override fun getProcessingOrders(callback: FirebaseCallback<List<Order>>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Orders")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (item in snapshot.children) {
                    val order = item.getValue(Order::class.java)
                    if (order != null) {
                        if (order.status == PROCESSING) {
                            orders.add(order)
                        }
                    }
                }
                orders.sortByDescending { it.timestamp }
                callback.onSuccess(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }

        })
    }

    override fun getOrdersByTimeRange(
        startTime: Long,
        endTime: Long,
        callback: FirebaseCallback<List<Order>>
    ) {
        val ref = FirebaseDatabase.getInstance().reference.child("Orders")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (user in snapshot.children) {
                    for (item in user.children) {
                        val order = item.getValue(Order::class.java)
                        if (order != null) {
                            if (order.timestamp in startTime..endTime
                                && order.status == SUCCESS
                            ) {
                                orders.add(order)
                            }
                        }
                    }
                }
                orders.sortByDescending { it.timestamp }
                callback.onSuccess(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }

        })
    }

    override fun getAllProcessingOrders(callback: FirebaseCallback<List<Order>>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Orders")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (user in snapshot.children) {
                    for (item in user.children) {
                        val order = item.getValue(Order::class.java)
                        if (order != null && Utils.isToday(order.timestamp)) {
                            if (order.status == PROCESSING) {
                                orders.add(order)
                            }
                        }
                    }
                }
                orders.sortByDescending { it.timestamp }
                callback.onSuccess(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }

        })
    }

    override fun getCompleteOrders(callback: FirebaseCallback<List<Order>>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Orders")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (item in snapshot.children) {
                    val order = item.getValue(Order::class.java)
                    if (order != null) {
                        if (order.status == SUCCESS) {
                            orders.add(order)
                        }
                    }
                }
                orders.sortByDescending { it.timestamp }
                callback.onSuccess(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }

        })
    }

    override fun getAllCompleteOrders(callback: FirebaseCallback<List<Order>>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Orders")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (user in snapshot.children) {
                    for (item in user.children) {
                        val order = item.getValue(Order::class.java)
                        if (order != null) {
                            if (order.status == SUCCESS && Utils.isToday(order.timestamp)) {
                                orders.add(order)
                            }
                        }
                    }
                }
                orders.sortByDescending { it.timestamp }
                callback.onSuccess(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }

        })
    }

    override fun getCancelOrders(callback: FirebaseCallback<List<Order>>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Orders")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (item in snapshot.children) {
                    val order = item.getValue(Order::class.java)
                    if (order != null) {
                        if (order.status == CANCEL) {
                            orders.add(order)
                        }
                    }
                }
                orders.sortByDescending { it.timestamp }
                callback.onSuccess(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }

        })
    }

    override fun getAllCancelOrders(callback: FirebaseCallback<List<Order>>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Orders")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (user in snapshot.children) {
                    for (item in user.children) {
                        val order = item.getValue(Order::class.java)
                        if (order != null) {
                            if (order.status == CANCEL && Utils.isToday(order.timestamp)) {
                                orders.add(order)
                            }
                        }
                    }
                }
                orders.sortByDescending { it.timestamp }
                callback.onSuccess(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }

        })
    }

    override fun editOrder(order: Order, callback: (Boolean) -> Unit) {

    }

    override fun deleteOrder(order: Order, callback: (Boolean) -> Unit) {

    }

    override fun addOrder(order: Order, callback: (Boolean) -> Unit) {

    }
}