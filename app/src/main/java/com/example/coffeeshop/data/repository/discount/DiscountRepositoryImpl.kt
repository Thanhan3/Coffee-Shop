package com.example.coffeeshop.data.repository.discount

import com.example.coffeeshop.data.DiscountDataSource
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Discount

class DiscountRepositoryImpl(private val remote: DiscountDataSource.remote): DiscountRepository  {
    override fun loadDiscount(callback: FirebaseCallback<List<Discount>>) {
        remote.loadDiscount(callback)
    }

    override fun addDiscount(discount: Discount, callback: (Boolean) -> Unit) {
        remote.addDiscount(discount,callback)
    }

    override fun deleteDiscount(discount: Discount, callback: (Boolean) -> Unit) {
        remote.deleteDiscount(discount,callback)
    }

    override fun getDiscountById(discountId: String, callback: FirebaseCallback<Discount>) {
        remote.getDiscountById(discountId,callback)
    }
}