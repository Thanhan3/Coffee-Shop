package com.example.coffeeshop.data

import com.example.coffeeshop.data.model.Category

interface CategoryDataSource {

    interface Remote {
        fun loadCategory(callback: FirebaseCallback<List<Category>>)
        fun addCategory(category: Category,callback: (Boolean) -> Unit)
        fun editCategory(category: Category,callback: (Boolean) -> Unit)
        fun deleteCategory(category: Category,callback: (Boolean) -> Unit)
    }
}