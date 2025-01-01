package com.example.coffeeshop.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rate(
    var idRate: String = "",
    var idUser: String = "",
    var idOrder: String = "",
    var idDrink: String = "",
    var rate: Int = 0,
    var comment: String = "",
    val timestamp: Long = System.currentTimeMillis(),
) : Parcelable