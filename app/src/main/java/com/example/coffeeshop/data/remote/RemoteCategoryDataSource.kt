package com.example.coffeeshop.data.remote

import com.example.coffeeshop.data.CategoryDataSource
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RemoteCategoryDataSource : CategoryDataSource.Remote {
    override fun loadCategory(callback: FirebaseCallback<List<Category>>) {
        val ref = FirebaseDatabase.getInstance().reference.child("Categories")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories = mutableListOf<Category>()
                for (item in snapshot.children) {
                    val category = item.getValue(Category::class.java)
                    if (category != null) {
                        categories.add(category)
                    }
                }
                callback.onSuccess(categories)
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onFailure(error.message)
            }
        })
    }

    override fun addCategory(category: Category, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance().reference.child("Categories")
        val key = ref.push().key
        if (key != null) {
            val categoryWithId = category.copy(id = key)
            ref.child(key).setValue(categoryWithId)
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

    override fun editCategory(category: Category, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance()
            .reference.child("Categories").child(category.id)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val categoryUpdates = mapOf<String, Any>(
                        "name" to category.name
                    )
                    ref.updateChildren(categoryUpdates)
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

    override fun deleteCategory(category: Category, callback: (Boolean) -> Unit) {
        val ref = FirebaseDatabase.getInstance()
            .reference.child("Categories").child(category.id)
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