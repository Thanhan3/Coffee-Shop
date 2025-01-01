package com.example.coffeeshop.ui.admin.setting.discount

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.coffeeshop.data.model.Discount
import com.example.coffeeshop.databinding.ItemDiscountBinding


class DiscountAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>() {
    private val discounts = mutableListOf<Discount>()

    inner class DiscountViewHolder(
        private val binding: ItemDiscountBinding
    ) : ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(discount: Discount) {
            binding.textDiscountTitle.text = "Giảm giá ${discount.discountPercent}%"
            binding.textMinPrice.text =
                "Áp dụng cho đơn hàng tối thiểu: ${discount.minPrice}.000vnd"
            binding.imgEdit.setOnClickListener {
                onItemClickListener.onEditClick(discount)
            }
            binding.imgDelete.setOnClickListener {
                onItemClickListener.onDeleteClick(discount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountViewHolder {
        return DiscountViewHolder(
            ItemDiscountBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return discounts.size
    }

    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
        holder.bind(discounts[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Discount>) {
        discounts.clear()
        discounts.addAll(data)
        notifyDataSetChanged()
    }
}

interface OnItemClickListener {
    fun onEditClick(discount: Discount)
    fun onDeleteClick(discount: Discount)
}