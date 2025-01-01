package com.example.coffeeshop.ui.user.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.Category
import com.example.coffeeshop.databinding.ItemTitleCategoryBinding

class TitleCategoryAdapter(
    private val onCategoryClickListener: onCategoryClickListener
) : RecyclerView.Adapter<TitleCategoryAdapter.ViewHolder>() {
    private val categories = mutableListOf<Category>()
    private var selectedPosition = -1
    inner class ViewHolder(
        private val binding: ItemTitleCategoryBinding
    ):RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(category: Category) {
            binding.textCategory.text = category.name
            if (selectedPosition == -1 && adapterPosition == 0) {
                onCategoryClickListener.onCategoryClick(category)
                binding.view.visibility = View.VISIBLE
                binding.textCategory.setTextColor(binding.root.context.getColor(R.color.colorPrimary))
            }else if(selectedPosition == adapterPosition) {
                binding.view.visibility = View.VISIBLE
                binding.textCategory.setTextColor(binding.root.context.getColor(R.color.colorPrimary))
            }else{
                binding.view.visibility = View.GONE
                binding.textCategory.setTextColor(binding.root.context.getColor(R.color.gray))
            }
            binding.root.setOnClickListener {
                selectedPosition = adapterPosition
                notifyDataSetChanged()
                onCategoryClickListener.onCategoryClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTitleCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
        notifyDataSetChanged()
    }
}

interface onCategoryClickListener {
    fun onCategoryClick(category: Category)
}