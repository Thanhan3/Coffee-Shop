package com.example.coffeeshop.ui.user.history.processing

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.databinding.ItemProcessingOrderBinding
import com.example.coffeeshop.utils.Utils

class ProcessingAdapter(private val onClickFollowOrder: OnClickFollowOrder) : Adapter<ProcessingAdapter.ProcessingViewHolder>() {

    private val orders = mutableListOf<Order>()

    inner class ProcessingViewHolder(
        private val binding: ItemProcessingOrderBinding
    ) : ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(order: Order) {
            binding.textOrderId.text = order.orderId
            val discount = order.discount
            if (discount == null) {
                binding.textPrice.text = order.price.toString() +".0OOvnd"
            } else {
                binding.textPrice.text =
                    Utils.getFinalPrice(order.price, discount.discountPercent).toString()+".0OOvnd"
            }
            var description = ""
            val cartItems = order.items
            for (item in cartItems) {
                description += "${item.drink.name},"
            }
            binding.textDescription.maxLines = 3
            binding.textDescription.ellipsize = android.text.TextUtils.TruncateAt.END
            binding.textDescription.text = description
            binding.textCountDrink.text = "(${cartItems.size} đồ uống)"
            val image = order.items[0].drink.image
            Glide.with(binding.root.context).load(image).into(binding.imgDrink)
            binding.textFollowOder.setOnClickListener {
                onClickFollowOrder.onClickFollowOrder(order)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessingViewHolder {
        return ProcessingViewHolder(
            ItemProcessingOrderBinding.inflate(
                android.view.LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: ProcessingViewHolder, position: Int) {
        return holder.bind(orders[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(orders: List<Order>) {
        this.orders.clear()
        this.orders.addAll(orders)
        notifyDataSetChanged()
    }

}

interface OnClickFollowOrder {
    fun onClickFollowOrder(order: Order)
}