package com.example.coffeeshop.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey
@Parcelize
@Entity(tableName = "toppings")
data class Topping(
    @PrimaryKey
    var id: String = "",
    var name: String = "",
    var price: Int = 0
):Parcelable