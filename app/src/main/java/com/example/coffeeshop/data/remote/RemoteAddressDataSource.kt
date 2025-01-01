package com.example.coffeeshop.data.remote

import android.util.Log
import com.example.coffeeshop.data.AddressDataSource
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Address
import com.example.coffeeshop.data.model.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RemoteAddressDataSource : AddressDataSource.remote {
    override fun getAddressDefault(callback: FirebaseCallback<Address>) {
        val ref = FirebaseDatabase.getInstance().reference
            .child("Addresses").child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var resultAddress = Address()
                for (item in snapshot.children) {
                    val address = item.getValue(Address::class.java)
                    if (address != null) {
                        if (address.default) {
                            resultAddress = address
                            break
                        }
                    }
                }
                callback.onSuccess(resultAddress)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }
        })
    }

    override fun loadAddress(callback: FirebaseCallback<List<Address>>) {
        val ref = FirebaseDatabase.getInstance().reference
            .child("Addresses").child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val addresses = mutableListOf<Address>()
                for (item in snapshot.children) {
                    val address = item.getValue(Address::class.java)
                    if (address != null) {
                        addresses.add(address)
                        Log.d("RemoteAddressDataSource", "Address: $address")
                    }
                }
                callback.onSuccess(addresses)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }
        })
    }

    override fun addAddress(address: Address, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance().reference.child("Addresses")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        val key = ref.push().key
        if (key != null) {
            val addressWithId = address.copy(addressId = key)
            ref.child(key).setValue(addressWithId)
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

    override fun editAddress(address: Address, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance().reference.child("Addresses")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val itemAddress = item.getValue(Address::class.java)
                    if (itemAddress != null) {
                        if (itemAddress.addressId == address.addressId) {
                            val addressUpdates = mapOf<String, Any>(
                                "default" to address.default,
                            )
                            ref.child(itemAddress.addressId).updateChildren(addressUpdates)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        callback(true)
                                    } else {
                                        callback(false)
                                    }
                                }
                        } else {
                            val addressUpdates = mapOf<String, Any>(
                                "default" to false,
                            )
                            ref.child(itemAddress.addressId).updateChildren(addressUpdates)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        callback(true)
                                    } else {
                                        callback(false)
                                    }
                                }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }

        })
    }

    override fun deleteAddress(address: Address, callback: (Boolean) -> Unit) {
    }
}