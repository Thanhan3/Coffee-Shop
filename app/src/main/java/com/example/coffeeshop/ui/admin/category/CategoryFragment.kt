package com.example.coffeeshop.ui.admin.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.coffeeshop.data.model.Category
import com.example.coffeeshop.data.remote.RemoteCategoryDataSource
import com.example.coffeeshop.data.repository.category.CategoryRepositoryImpl
import com.example.coffeeshop.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private val remoteCategoryDataSource = RemoteCategoryDataSource()
    private val categoryRepositoryImpl = CategoryRepositoryImpl(remoteCategoryDataSource)
    private val categoryViewModelFactory = CategoryViewModel.Factory(categoryRepositoryImpl)
    private val categoryViewModel: CategoryViewModel by activityViewModels {
        categoryViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupView()
        setupAddCategory()
    }

    private fun setupAddCategory() {
        binding.fabAddCategory.setOnClickListener {
            val intent = Intent(requireContext(), com.example.coffeeshop.ui.admin.category.AddCategoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupView() {
        categoryAdapter = CategoryAdapter(
            object : onCategoryClickListener {
                override fun onItemClick(category: Category) {
                    val intent = Intent(requireContext(), DetailCategoryActivity::class.java)
                    intent.putExtra("categoryId", category.id)
                    intent.putExtra("categoryName", category.name)
                    startActivity(intent)
                }
            },
            object : onDeleteClickListener {
                override fun onDeleteClick(category: Category) {
                    categoryViewModel.deleteCategory(category) { isSuccess ->
                        if (isSuccess) {
                            Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(requireContext(), "Xóa thất bại", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            },
            object : onEditClickListener {
                override fun onEditClick(category: Category) {
                    val intent = Intent(requireContext(), EditCategoryActivity::class.java)
                    intent.putExtra("categoryId", category.id)
                    intent.putExtra("categoryName", category.name)
                    startActivity(intent)
                }
            }
        )
        binding.rvCategory.adapter = categoryAdapter
    }

    private fun setupObserver() {
        categoryViewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.updateData(it)
        }
    }

}