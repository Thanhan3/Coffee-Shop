package com.example.coffeeshop.data.model
import java.io.Serializable

data class Discount(
    val id: String = "",
    val discountPercent : Int = 0,
    val minPrice:Int = 0
): Serializable