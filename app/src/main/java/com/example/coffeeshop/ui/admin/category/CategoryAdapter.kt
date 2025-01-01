package com.example.coffeeshop.ui.admin.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeshop.data.model.Category
import com.example.coffeeshop.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val onCategoryClickListener: onCategoryClickListener,
    private val onDeleteClickListener: onDeleteClickListener,
    private val onEditClickListener: onEditClickListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private val categoryList = mutableListOf<Category>()
    class ViewHolder(
        private val binding: ItemCategoryBinding,
        private val onCategoryClickListener: onCategoryClickListener,
        private val onDeleteClickListener: onDeleteClickListener,
        private val onEditClickListener: onEditClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.tvCategory.text = category.name
            binding.imageDelete.setOnClickListener {
                onDeleteClickListener.onDeleteClick(category)
            }
            binding.imageEdit.setOnClickListener {
                onEditClickListener.onEditClick(category)
            }
            binding.root.setOnClickListener {
                onCategoryClickListener.onItemClick(category)
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),onCategoryClickListener,onDeleteClickListener,onEditClickListener
        )
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(categoryList: List<Category>) {
        this.categoryList.clear()
        this.categoryList.addAll(categoryList)
        notifyDataSetChanged()
    }

}
interface onCategoryClickListener {
    fun onItemClick(category: Category)
}
interface onDeleteClickListener {
    fun onDeleteClick(category: Category)
}
interface onEditClickListener {
    fun onEditClick(category: Category)
}