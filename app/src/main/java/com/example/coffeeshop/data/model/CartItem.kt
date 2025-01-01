package com.example.coffeeshop.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.coffeeshop.data.local.ToppingConverter

@Parcelize
@Entity(tableName = "cart")
data class CartItem(
    @PrimaryKey
    var cartId: String = "",
    @Embedded
    var drink: Drink = Drink(),
    var count: Int = 0,
    @TypeConverters(ToppingConverter::class)
    var topping: List<Topping> = emptyList(),
    var totalPrice: Int = 0,
    var note: String = "",
    var isHot: Boolean = false,
    var isLessSugar: Boolean = false,
    var isLessIce: Boolean = false
) : Parcelable