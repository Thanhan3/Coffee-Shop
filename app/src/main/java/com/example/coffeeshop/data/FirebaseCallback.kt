package com.example.coffeeshop.data

interface FirebaseCallback<T> {
    fun onSuccess(data: T)
    fun onFailure(error: String)
}