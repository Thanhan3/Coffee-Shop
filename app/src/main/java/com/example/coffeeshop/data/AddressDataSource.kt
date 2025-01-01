package com.example.coffeeshop.data

import com.example.coffeeshop.data.model.Address
import com.example.coffeeshop.data.model.Category

interface AddressDataSource {
    interface remote {
        fun getAddressDefault(callback: FirebaseCallback<Address>)
        fun loadAddress(callback: FirebaseCallback<List<Address>>)
        fun addAddress(address: Address, callback: (Boolean) -> Unit)
        fun editAddress(address: Address, callback: (Boolean) -> Unit)
        fun deleteAddress(address: Address, callback: (Boolean) -> Unit)
    }
}