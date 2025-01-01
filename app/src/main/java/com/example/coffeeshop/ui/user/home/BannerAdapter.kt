package com.example.coffeeshop.ui.user.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.coffeeshop.data.model.Drink
import com.example.coffeeshop.databinding.ItemBannerBinding

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    private val drinks = mutableListOf<Drink>()
    class BannerViewHolder(private val binding: ItemBannerBinding):ViewHolder(binding.root) {
        fun bind(drink: Drink){
            Glide.with(binding.root).load(drink.image).into(binding.imageDrink)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(
            ItemBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return drinks.size
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(drinks[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newDrinks: List<Drink>) {
        drinks.clear()
        drinks.addAll(newDrinks)
        notifyDataSetChanged()
    }
}