package com.example.coffeeshop.data.repository.category

import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Category

interface CategoryRepository {
    fun loadCategory(callback: FirebaseCallback<List<Category>>)
    fun addCategory(category: Category,callback: (Boolean) -> Unit)
    fun deleteCategory(category: Category,callback: (Boolean) -> Unit)
    fun editCategory(category: Category,callback: (Boolean) -> Unit)
}