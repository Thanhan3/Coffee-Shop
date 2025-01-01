package com.example.coffeeshop.data.remote

import com.example.coffeeshop.data.DiscountDataSource
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Discount
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RemoteDiscountDataSource : DiscountDataSource.remote {
    override fun loadDiscount(callback: FirebaseCallback<List<Discount>>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Discounts")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val discounts = mutableListOf<Discount>()
                for (item in snapshot.children) {
                    val discount = item.getValue(Discount::class.java)
                    if (discount != null) {
                        discounts.add(discount)
                    }
                }
                callback.onSuccess(discounts)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }
        })
    }

    override fun addDiscount(discount: Discount, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance().reference.child("Discounts")
        val key = ref.push().key
        if (key != null) {
            val newDiscount = discount.copy(id = key)
            ref.child(key).setValue(newDiscount)
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

    override fun deleteDiscount(discount: Discount, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance()
            .reference.child("Discounts").child(discount.id)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ref.removeValue()
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

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }

    override fun getDiscountById(discountId: String, callback: FirebaseCallback<Discount>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Discounts").child(discountId)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val discount = snapshot.getValue(Discount::class.java)
                    if (discount != null) {
                        callback.onSuccess(discount)
                    } else {
                        callback.onFailure("Discount not found")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }
        })
    }

    override fun editDiscount(discount: Discount, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance()
            .reference.child("Discounts").child(discount.id)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val discountUpdates = mapOf<String, Any>(
                        "discountPercent" to discount.discountPercent,
                        "minPrice" to discount.minPrice
                    )
                    ref.updateChildren(discountUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                callback(true)
                            } else {
                                callback(false)
                            }
                        }
                } else {
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }
}