package com.example.coffeeshop.ui.user.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.data.model.CartItem
import com.example.coffeeshop.databinding.ItemDrinkReceiptBinding

class ConfirmAdapter : RecyclerView.Adapter<ConfirmAdapter.ViewHolder>() {
    private val cartItems = mutableListOf<CartItem>()

    class ViewHolder(
        private val binding: ItemDrinkReceiptBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cartItem: CartItem) {
            val drink = cartItem.drink
            val count = cartItem.count
            setupDescription(cartItem)
            Glide.with(binding.root.context).load(drink.image).into(binding.imgDrink)
            binding.textNameDrink.text = drink.name
            binding.textPrice.text = drink.price.toString()+".000vnd"
            binding.textCount.text = "x$count"
        }

        private fun setupDescription(cartItem: CartItem) {
            var description = ""
            if (cartItem.isHot) {
                description += "Đồ uống nóng"
            } else {
                description += "Đồ uống lạnh"
                if (cartItem.isLessIce) {
                    description += ", giảm đá"
                }
            }
            if (cartItem.isLessSugar) {
                description += ", giảm đường"
            }
            val listTopping = cartItem.topping
            if (listTopping.isNotEmpty()) {
                description += ", Topping: "
            }
            for (topping in listTopping) {
                description += ", ${topping.name}"
            }
            if (cartItem.note.isNotEmpty()) {
                description += ", Ghi chú: ${cartItem.note.trim()}"
            }
            binding.textDescription.text = description
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDrinkReceiptBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(cartItems: List<CartItem>) {
        this.cartItems.clear()
        this.cartItems.addAll(cartItems)
        notifyDataSetChanged()
    }
}