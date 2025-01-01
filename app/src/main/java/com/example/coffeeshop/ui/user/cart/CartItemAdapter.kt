package com.example.coffeeshop.ui.user.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.coffeeshop.data.model.CartItem
import com.example.coffeeshop.databinding.ItemCartBinding
import com.example.coffeeshop.ui.user.product.UserDrinkActivity

class CartItemAdapter(
    private val onCartItemChange: OnCartItemChange
) : Adapter<CartItemAdapter.ViewHolder>() {
    private val cartItems = mutableListOf<CartItem>()

    inner class ViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cartItem: CartItem) {
            val drink = cartItem.drink
            binding.textNameDrink.text = drink.name
            Glide.with(binding.root.context).load(drink.image).into(binding.imgDrink)
            val price = cartItem.totalPrice
            binding.textPrice.text = "$price.000vnd"
            setupDescription(cartItem)
            binding.textCount.text = "x${cartItem.count}"
            binding.edCount.setText(cartItem.count.toString())
            binding.imageAdd.setOnClickListener {
                var count = binding.edCount.text.toString().toInt()
                val basePrice = cartItem.totalPrice/cartItem.count
                count++
                binding.edCount.setText(count.toString())
                val newPrice = count*basePrice
                val newItemCount = count
                val newCartItem = cartItem.copy(count = newItemCount,totalPrice = newPrice)
                onCartItemChange.onCountChange(newCartItem)
            }
            binding.imageMinus.setOnClickListener {
                var count = binding.edCount.text.toString().toInt()
                val basePrice = cartItem.totalPrice/cartItem.count
                if (count > 1) {
                    count--
                    binding.edCount.setText(count.toString())
                    val newPrice = count*basePrice
                    val newItemCount = count
                    val newCartItem = cartItem.copy(count = newItemCount,totalPrice = newPrice)
                    onCartItemChange.onCountChange(newCartItem)
                }else{
                    onCartItemChange.onCartItemDelete(cartItem)
                }
            }
            binding.imgEdit.setOnClickListener{
                val intent = Intent(binding.root.context, UserDrinkActivity::class.java)
                intent.putExtra("drinkId", drink.id)
                binding.root.context.startActivity(intent)
            }
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
            if(listTopping.isNotEmpty()){
                description+= ", Topping: "
            }
            for(topping in listTopping){
                description+= ", ${topping.name}"
            }
            if (cartItem.note.isNotEmpty()) {
                description += ", Ghi chú: ${cartItem.note.trim()}"
            }
            binding.textDescription.text = description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCartBinding.inflate(
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
    fun setCartItems(newCartItems: List<CartItem>) {
        val diffResult = DiffUtil.calculateDiff(CartItemDiffCallback(cartItems, newCartItems))
        cartItems.clear()
        cartItems.addAll(newCartItems)
        diffResult.dispatchUpdatesTo(this)
    }


    class CartItemDiffCallback(
        private val oldList: List<CartItem>,
        private val newList: List<CartItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].cartId == newList[newItemPosition].cartId

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }

}

interface OnCartItemChange {
   fun onCountChange(cartItem: CartItem)
   fun onCartItemDelete(cartItem: CartItem)
}