package com.example.coffeeshop.data.remote

import com.example.coffeeshop.data.DrinkDataSource
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Drink
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RemoteDrinkDataSource : DrinkDataSource.Remote {
    override fun loadDrink(callback: FirebaseCallback<List<Drink>>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Drinks")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val drinks = mutableListOf<Drink>()
                for (item in snapshot.children) {
                    val drink = item.getValue(Drink::class.java)
                    if (drink != null) {
                        drinks.add(drink)
                    }
                }
                callback.onSuccess(drinks)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }
        })
    }

    override fun addDrink(drink: Drink, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance().reference.child("Drinks")
        val key = ref.push().key
        if (key != null) {
            val drinkWithId = drink.copy(id = key)
            ref.child(key).setValue(drinkWithId)
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

    override fun deleteDrink(drink: Drink, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance()
            .reference.child("Drinks").child(drink.id)
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
                } else {
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }

    override fun getDrinkByCategory(categoryId: String, callback: FirebaseCallback<List<Drink>>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Drinks")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val drinks = mutableListOf<Drink>()
                for (item in snapshot.children) {
                    val drink = item.getValue(Drink::class.java)
                    if (drink != null) {
                        if (drink.category == categoryId) {
                            drinks.add(drink)
                        }
                    }
                }
                callback.onSuccess(drinks)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }
        })
    }

    override fun editDrink(drink: Drink, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance()
            .reference.child("Drinks").child(drink.id)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val drinkUpdate = mapOf<String, Any>(
                        "id" to drink.id,
                        "name" to drink.name,
                        "description" to drink.description,
                        "category" to drink.category,
                        "price" to drink.price,
                        "image" to drink.image,
                        "discount" to drink.discount,
                        "outstanding" to drink.outstanding
                    )
                    ref.updateChildren(drinkUpdate)
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

    override fun getDrinkById(drinkId: String, callback: FirebaseCallback<Drink>) {
        val ref = FirebaseDatabase.getInstance()
            .reference.child("Drinks").child(drinkId)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val drink = snapshot.getValue(Drink::class.java)
                    if (drink != null) {
                        callback.onSuccess(drink)
                    } else {
                        callback.onFailure("Drink not found")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure("Drink not found")
            }

        })
    }

    override fun getOutStandingDrink(callback: FirebaseCallback<List<Drink>>) {
        val ref = FirebaseDatabase.getInstance()
            .reference.child("Drinks")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val drinks = mutableListOf<Drink>()
                for (item in snapshot.children) {
                    val drink = item.getValue(Drink::class.java)
                    if (drink != null) {
                        if (drink.outstanding) {
                            drinks.add(drink)
                        }
                    }
                }
                callback.onSuccess(drinks)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }
        })
    }
}