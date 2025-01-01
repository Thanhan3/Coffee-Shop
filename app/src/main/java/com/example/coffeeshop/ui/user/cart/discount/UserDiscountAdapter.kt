package com.example.coffeeshop.ui.user.cart.discount

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeshop.data.model.Discount
import com.example.coffeeshop.databinding.ItemUserDiscountBinding

class UserDiscountAdapter(
    private val onDiscountClick: OnDiscountClick
) : RecyclerView.Adapter<UserDiscountAdapter.UserDiscountViewHolder>() {
    private val userDiscounts = mutableListOf<Discount>()

    inner class UserDiscountViewHolder(
        private val binding: ItemUserDiscountBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(discount: Discount) {
            binding.textDiscountTitle.text = "Giảm giá ${discount.discountPercent}%"
            binding.textMinPrice.text =
                "Áp dụng cho đơn hàng tối thiểu: ${discount.minPrice}.000vnd"
            binding.root.setOnClickListener {
                onDiscountClick.onItemDiscountClick(discount)
            }
            binding.root.isEnabled= onDiscountClick.isDiscountAvailable(discount)
            if(!onDiscountClick.isDiscountAvailable(discount)){
                binding.root.alpha = 0.5f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDiscountViewHolder {
        return UserDiscountViewHolder(
            ItemUserDiscountBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return userDiscounts.size
    }

    override fun onBindViewHolder(holder: UserDiscountViewHolder, position: Int) {
        holder.bind(userDiscounts[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Discount>) {
        userDiscounts.clear()
        userDiscounts.addAll(newData)
        notifyDataSetChanged()
    }
}

interface OnDiscountClick{
    fun onItemDiscountClick(discount: Discount)
    fun isDiscountAvailable(discount: Discount): Boolean
}