package com.example.coffeeshop.data.repository.discount

import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Discount

interface DiscountRepository {
    fun loadDiscount(callback: FirebaseCallback<List<Discount>>)
    fun addDiscount(discount: Discount,callback: (Boolean) -> Unit)
    fun deleteDiscount(discount: Discount,callback: (Boolean) -> Unit)
    fun getDiscountById(discountId: String, callback: FirebaseCallback<Discount>)

}