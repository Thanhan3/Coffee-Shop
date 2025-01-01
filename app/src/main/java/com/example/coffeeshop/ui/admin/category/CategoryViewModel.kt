package com.example.coffeeshop.ui.admin.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Category
import com.example.coffeeshop.data.repository.category.CategoryRepositoryImpl

class CategoryViewModel(
    private val repository: CategoryRepositoryImpl
) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _categories


    init {
        loadCategory()
    }

    private fun loadCategory() {
        repository.loadCategory(object : FirebaseCallback<List<Category>> {
            override fun onSuccess(data: List<Category>) {
                _categories.postValue(data)
            }

            override fun onFailure(error: String) {
                _categories.postValue(emptyList())
            }
        })

    }

    fun editCategory(category: Category, callback: (Boolean) -> Unit) {
        repository.editCategory(category) { isSuccess ->
            callback(isSuccess)
        }
    }

    fun addCategory(category: Category, callback: (Boolean) -> Unit) {
        repository.addCategory(category) { isSuccess ->
            callback(isSuccess)
        }
    }

    fun deleteCategory(category: Category, callback: (Boolean) -> Unit) {
        repository.deleteCategory(category) { isSuccess ->
            callback(isSuccess)
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory (
        private val categoryRepository: CategoryRepositoryImpl
    ):ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
                return CategoryViewModel(categoryRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}