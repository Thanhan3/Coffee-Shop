package com.example.coffeeshop.data

import com.example.coffeeshop.data.model.Discount

interface DiscountDataSource {
    interface remote{
        fun loadDiscount(callback: FirebaseCallback<List<Discount>>)
        fun addDiscount(discount: Discount,callback: (Boolean) -> Unit)
        fun deleteDiscount(discount: Discount,callback: (Boolean) -> Unit)
        fun getDiscountById(discountId: String, callback: FirebaseCallback<Discount>)
        fun editDiscount(discount: Discount,callback: (Boolean) -> Unit)
    }
}