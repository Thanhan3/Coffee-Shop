package com.example.coffeeshop.data.model

import android.os.Parcelable
import com.example.coffeeshop.utils.Constant.PROCESSING
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val orderId: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val discount: Discount? = null,
    val paymentMethod: Int = 0,
    val price: Int = 0,
    val address: Address? = null,
    val finalPrice: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val shopStatus: Int = 0,
    val status: Int = PROCESSING
): Parcelable
