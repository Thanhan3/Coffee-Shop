package com.example.coffeeshop.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


import androidx.room.Entity
import androidx.room.PrimaryKey

@Parcelize
@Entity(tableName = "drinks")
data class Drink(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val price: Int = 0,
    val discount: Int = 0,
    val image: String = "",
    val outstanding: Boolean = false
):Parcelable
