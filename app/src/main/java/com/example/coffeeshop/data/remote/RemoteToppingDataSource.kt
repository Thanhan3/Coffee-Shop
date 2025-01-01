package com.example.coffeeshop.data.remote

import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.ToppingDataSource
import com.example.coffeeshop.data.model.Drink
import com.example.coffeeshop.data.model.Topping
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RemoteToppingDataSource: ToppingDataSource.Remote {
    override fun loadTopping(callback: FirebaseCallback<List<Topping>>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Toppings")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val toppings = mutableListOf<Topping>()
                for (item in snapshot.children) {
                    val topping = item.getValue(Topping::class.java)
                    if (topping != null) {
                        toppings.add(topping)
                    }
                }
                callback.onSuccess(toppings)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }
        })
    }

    override fun addTopping(topping: Topping, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance().reference.child("Toppings")
        val key = ref.push().key
        if (key != null) {
            val toppingId = topping.copy(id = key)
            ref.child(key).setValue(toppingId)
                .addOnSuccessListener {
                    callback(true)
                }
                .addOnFailureListener {
                    callback(false)
                }
        } else {
            callback(false)
        }
    }

    override fun editTopping(topping: Topping, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance()
            .reference.child("Toppings").child(topping.id)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val toppingUpdates = mapOf<String, Any>(
                        "name" to topping.name,
                        "price" to topping.price,
                    )
                    ref.updateChildren(toppingUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                callback(true)
                            } else {
                                callback(false)
                            }
                        }
                }else{
                    callback(false)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }

    override fun deleteTopping(topping: Topping, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance()
            .reference.child("Toppings").child(topping.id)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ref.removeValue()
                        .addOnSuccessListener {
                            callback(true)
                        }
                        .addOnFailureListener {
                            callback(false)
                        }
                }else{
                    callback(false)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }


}