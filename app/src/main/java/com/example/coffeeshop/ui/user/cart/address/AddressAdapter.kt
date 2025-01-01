package com.example.coffeeshop.ui.user.cart.address

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.coffeeshop.data.model.Address
import com.example.coffeeshop.databinding.ItemAddressBinding

class AddressAdapter(
    private val onAddressClickListener: OnAddressClickListener
) : Adapter<AddressAdapter.AddressViewHolder>() {

    private val addresses = mutableListOf<Address>()

    inner class AddressViewHolder(private val binding: ItemAddressBinding)
        :ViewHolder(binding.root) {
        fun bind(address: Address) {
            binding.tvName.text = address.receiverName
            binding.tvPhone.text = address.numberPhone
            binding.tvAddress.text = address.address
            binding.checkBox.isChecked = address.default
            binding.checkBox.setOnClickListener{
                address.default = binding.checkBox.isChecked
                onAddressClickListener.onAddressClick(address)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            ItemAddressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return addresses.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addresses[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newAddresses: List<Address>) {
        addresses.clear()
        addresses.addAll(newAddresses)
        notifyDataSetChanged()
    }
}

interface OnAddressClickListener {
    fun onAddressClick(address: Address)
}