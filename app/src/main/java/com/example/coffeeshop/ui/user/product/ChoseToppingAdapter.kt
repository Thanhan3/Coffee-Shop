package com.example.coffeeshop.ui.user.product

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.coffeeshop.data.model.Topping
import com.example.coffeeshop.databinding.ItemChoseToppingBinding

class ChoseToppingAdapter(
    val itemListener: ToppingListener
): RecyclerView.Adapter<ChoseToppingAdapter.ChoseToppingViewHolder>() {
    private var toppings = mutableListOf<Topping>()
    private var selectedTopping = mutableListOf<Topping>()
    inner class ChoseToppingViewHolder(
        private val binding: ItemChoseToppingBinding
    ):ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(topping: Topping){
            binding.checkBox.isChecked = selectedTopping.contains(topping)
            val price = topping.price
            binding.textToppingName.text = topping.name
            binding.textPrice.text = "+${price}.000vnd"
            binding.checkBox.setOnClickListener {
                if (binding.checkBox.isChecked){
                    itemListener.onSelected(topping)
                }else{
                    itemListener.onUnSelected(topping)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoseToppingViewHolder {
        return ChoseToppingViewHolder(
            ItemChoseToppingBinding.inflate(
                android.view.LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return toppings.size
    }

    override fun onBindViewHolder(holder: ChoseToppingViewHolder, position: Int) {
        holder.bind(toppings[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTopping(toppings: List<Topping>){
        this.toppings.clear()
        this.toppings.addAll(toppings)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateSelectedTopping(selectedTopping: List<Topping>){
        this.selectedTopping.clear()
        this.selectedTopping.addAll(selectedTopping)
        notifyDataSetChanged()
    }
}

interface ToppingListener{
    fun onSelected(topping: Topping){

    }
    fun onUnSelected(topping: Topping){

    }
}