package com.example.coffeeshop.data.repository.address

import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Address

interface AddressRepository {
    fun getAddressDefault(callback: FirebaseCallback<Address>)
    fun loadAddress(callback: FirebaseCallback<List<Address>>)
    fun addAddress(address: Address, callback: (Boolean) -> Unit)
    fun editAddress(address: Address, callback: (Boolean) -> Unit)
    fun deleteAddress(address: Address, callback: (Boolean) -> Unit)
}