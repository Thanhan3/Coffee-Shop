package com.example.coffeeshop.data.repository.address

import com.example.coffeeshop.data.AddressDataSource
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Address

class AddressRepositoryImpl(
    private val remote: AddressDataSource.remote
) : AddressRepository {
    override fun getAddressDefault(callback: FirebaseCallback<Address>) {
        remote.getAddressDefault(callback)
    }

    override fun loadAddress(callback: FirebaseCallback<List<Address>>) {
        remote.loadAddress(callback)
    }

    override fun addAddress(address: Address, callback: (Boolean) -> Unit) {
        remote.addAddress(address, callback)
    }

    override fun editAddress(address: Address, callback: (Boolean) -> Unit) {
        remote.editAddress(address, callback)
    }

    override fun deleteAddress(address: Address, callback: (Boolean) -> Unit) {
        remote.deleteAddress(address, callback)
    }
}