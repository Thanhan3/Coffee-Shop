package com.example.coffeeshop.ui.user.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.coffeeshop.data.model.Drink
import com.example.coffeeshop.data.model.Rate
import com.example.coffeeshop.databinding.ItemUserDrinkBinding
import com.example.coffeeshop.ui.user.product.UserDrinkActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DrinkAdapter : RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder>() {
    private val drinks = mutableListOf<Drink>()

    class DrinkViewHolder(
        private val binding: ItemUserDrinkBinding
    ) : ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "DefaultLocale")
        fun bind(drink: Drink) {
            Glide.with(binding.root.context).load(drink.image).into(binding.imageDrink)
            binding.textName.text = drink.name
            binding.textDescription.text = drink.description
            val discount = drink.discount
            val price = drink.price * (100 - discount) / 100
            val priceBeforeDiscount = drink.price
            binding.textPrice.text = "$price.000vnd"
            if (discount > 0) {
                binding.textPriceBeforeDiscount.visibility = View.VISIBLE
                binding.textPriceBeforeDiscount.text = "$priceBeforeDiscount.000vnd"
                binding.textPriceBeforeDiscount.paintFlags =
                    binding.textPriceBeforeDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.textPriceBeforeDiscount.visibility = View.GONE
            }
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, UserDrinkActivity::class.java)
                intent.putExtra("drinkId", drink.id)
                binding.root.context.startActivity(intent)
            }
            getRate(drink.id) { rates ->
                if (rates.isNotEmpty()) {
                    val sumRate = rates.sumOf { it.rate }
                    val averageRate = (sumRate.toDouble()/ rates.size)
                    val formattedRate = String.format("%.1f", averageRate)
                    binding.textRating.text =formattedRate
                } else {
                    binding.textRating.text = "0"
                }

            }
        }

        private fun getRate(drinkId: String, callback: (List<Rate>) -> Unit) {
            val ref = FirebaseDatabase.getInstance().reference.child("Rates").child(drinkId)
            ref.addValueEventListener(object : ValueEventListener {
                val listRate = mutableListOf<Rate>()
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val rate = data.getValue(Rate::class.java)
                        if (rate != null) {
                            listRate.add(rate)
                        }
                    }
                    callback(listRate)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList())
                }
            }
            )
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        return DrinkViewHolder(
            ItemUserDrinkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return drinks.size
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        holder.bind(drinks[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newDrinks: List<Drink>) {
        drinks.clear()
        drinks.addAll(newDrinks)
        notifyDataSetChanged()
    }
}