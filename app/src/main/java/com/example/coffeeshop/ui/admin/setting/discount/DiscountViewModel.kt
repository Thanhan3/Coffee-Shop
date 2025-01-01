package com.example.coffeeshop.ui.admin.setting.discount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Discount
import com.example.coffeeshop.data.repository.discount.DiscountRepositoryImpl

class DiscountViewModel(
    private val repository: DiscountRepositoryImpl
):ViewModel() {
    private val _discounts = MutableLiveData<List<Discount>>()
    val discounts: LiveData<List<Discount>>
        get() = _discounts


    init {
        loadDiscount()
    }

    private fun loadDiscount() {
        repository.loadDiscount(object : FirebaseCallback<List<Discount>> {
            override fun onSuccess(data: List<Discount>) {
                _discounts.value = data
            }

            override fun onFailure(error: String) {
                _discounts.value = emptyList()
            }
        })
    }

    fun addDiscount(discount: Discount, callback: (Boolean) -> Unit) {
        repository.addDiscount(discount) { isSuccess ->
            callback(isSuccess)
        }
    }

    fun deleteDiscount(discount: Discount, callback: (Boolean) -> Unit) {
        repository.deleteDiscount(discount) { isSuccess ->
            callback(isSuccess)
        }
    }

    fun getDiscountById(discountId: String, callback: (Discount?) -> Unit) {
        repository.getDiscountById(discountId, object : FirebaseCallback<Discount> {
            override fun onSuccess(data: Discount) {
                callback(data)
            }

            override fun onFailure(error: String) {
                callback(null)
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: DiscountRepositoryImpl
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DiscountViewModel::class.java)) {
                return DiscountViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}