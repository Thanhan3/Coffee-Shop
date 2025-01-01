package com.example.coffeeshop.data.repository.category

import com.example.coffeeshop.data.CategoryDataSource
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Category

class CategoryRepositoryImpl(private val remote: CategoryDataSource.Remote): CategoryRepository  {
    override fun loadCategory(callback: FirebaseCallback<List<Category>>) {
        remote.loadCategory(callback)
    }

    override fun addCategory(category: Category,callback: (Boolean) -> Unit) {
        remote.addCategory(category,callback)
    }

    override fun deleteCategory(category: Category, callback: (Boolean) -> Unit) {
        remote.deleteCategory(category,callback)
    }

    override fun editCategory(category: Category, callback: (Boolean) -> Unit) {
        remote.editCategory(category,callback)
    }
}