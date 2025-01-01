package com.example.coffeeshop.ui.admin.setting.topping

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.coffeeshop.data.model.Topping
import com.example.coffeeshop.databinding.ItemToppingBinding

class ToppingAdapter : RecyclerView.Adapter<ToppingAdapter.ToppingViewHolder>() {

    private val toppings = mutableListOf<Topping>()
    class ToppingViewHolder(private val binding: ItemToppingBinding) :ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(topping: Topping){
            binding.tvNameTopping.text= topping.name
            binding.tvPriceTopping.text = "${topping.price}.000vnd"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToppingViewHolder {
        return ToppingViewHolder(
            ItemToppingBinding.inflate(
                android.view.LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return toppings.size
    }

    override fun onBindViewHolder(holder: ToppingViewHolder, position: Int) {
        holder.bind(toppings[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(toppings: List<Topping>) {
        this.toppings.clear()
        this.toppings.addAll(toppings)
        notifyDataSetChanged()
    }

}