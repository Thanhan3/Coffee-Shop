package com.example.coffeeshop.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    var addressId: String = "",
    var receiverName: String = "",
    var numberPhone: String = "",
    var address: String = "",
    var default: Boolean = false
) : Parcelable
