package com.example.coffeeshop.ui.admin.drink

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.data.model.Category
import com.example.coffeeshop.data.model.Drink

import com.example.coffeeshop.databinding.ItemDrinkBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class DrinkAdapter(
    private val onDeleteClickListener: onDrinkDeleteClickListener,
    private val onEditClickListener: onDrinkEditClickListener
) : RecyclerView.Adapter<DrinkAdapter.ViewHolder>() {
    private val drinkList = mutableListOf<Drink>()

    class ViewHolder(
        private val binding: ItemDrinkBinding,
        private val onDeleteClickListener: onDrinkDeleteClickListener,
        private val onEditClickListener: onDrinkEditClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(drink: Drink) {
            val discount = drink.discount
            val price = drink.price * (100 - discount) / 100
            val priceBeforeDiscount = drink.price
            binding.textDrinkName.text = drink.name
            binding.textPrice.text = "$price.000vnd"
            if (discount > 0) {
                binding.textPriceBeforeDiscount.visibility = View.VISIBLE
                binding.textPriceBeforeDiscount.text = "$priceBeforeDiscount.000vnd"
                binding.textPriceBeforeDiscount.paintFlags =
                    binding.textPriceBeforeDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.textPriceBeforeDiscount.visibility = View.GONE
            }
            binding.textOutstandingResult.text = if (drink.outstanding) "Có" else "Không"
            Glide.with(binding.root).load(drink.image).into(binding.imgDrink)
            getCategoryName(drink.category) { categoryName ->
                binding.textCategoryName.text = categoryName
            }
            binding.imageDelete.setOnClickListener {
                onDeleteClickListener.onDeleteClick(drink)
            }
            binding.imageEdit.setOnClickListener {
                onEditClickListener.onEditClick(drink)
            }
        }

        private fun getCategoryName(categoryId: String, callback: (String) -> Unit) {
            val ref = FirebaseDatabase.getInstance()
                .reference.child("Categories").child(categoryId)

            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val category = snapshot.getValue(Category::class.java)
                        if (category != null) {
                            callback(category.name)
                        }

                    } else {
                        callback("")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDrinkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onDeleteClickListener,
            onEditClickListener
        )
    }

    override fun getItemCount(): Int {
        return drinkList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(drinkList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(drinkList: List<Drink>) {
        this.drinkList.clear()
        this.drinkList.addAll(drinkList)
        notifyDataSetChanged()
    }
}

interface onDrinkDeleteClickListener {
    fun onDeleteClick(drink: Drink)
}

interface onDrinkEditClickListener {
    fun onEditClick(drink: Drink)
}