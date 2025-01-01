package com.example.coffeeshop.ui.user.history.complete

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.coffeeshop.data.model.CartItem
import com.example.coffeeshop.data.model.Rate
import com.example.coffeeshop.databinding.ItemDetailUserCompleteOrderBinding

class DetailAdapter(
    private val onRateClickListener: OnRateClickListener
) : RecyclerView.Adapter<DetailAdapter.DetailUserViewHolder>() {
    private val cartItems = mutableListOf<CartItem>()

    inner class DetailUserViewHolder(
        private val binding: ItemDetailUserCompleteOrderBinding
    ) : ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cartItem: CartItem) {
            val drink = cartItem.drink
            binding.textDrinkName.text = drink.name
            Glide.with(binding.root).load(drink.image).into(binding.imgDrink)
            onRateClickListener.getRate(drink.id){
                if(it!=null){
                    binding.textRating.text= it.rate.toString()
                    binding.textComment.text = it.comment
                }else{
                    binding.textRating.text="0"
                }
            }
            binding.textPrice.text =
                (cartItem.totalPrice / cartItem.count).toString() + ".000vnd"
            binding.layoutRating.setOnClickListener {
                onRateClickListener.onRateClick(cartItem)
            }
            setupDescription(cartItem)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailUserViewHolder {
        return DetailUserViewHolder(
            ItemDetailUserCompleteOrderBinding.inflate(
                android.view.LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun onBindViewHolder(holder: DetailUserViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(cartItems: List<CartItem>) {
        this.cartItems.clear()
        this.cartItems.addAll(cartItems)
        notifyDataSetChanged()
    }
}

interface OnRateClickListener {
    fun onRateClick(cartItem: CartItem)
    fun getRate(idDrink: String, callback :(Rate?)->Unit)
}