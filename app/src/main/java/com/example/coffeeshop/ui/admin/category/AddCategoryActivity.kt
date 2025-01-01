package com.example.coffeeshop.ui.admin.category

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.Category
import com.example.coffeeshop.data.remote.RemoteCategoryDataSource
import com.example.coffeeshop.data.repository.category.CategoryRepositoryImpl
import com.example.coffeeshop.databinding.ActivityAddCategoryBinding

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private val remoteCategoryDataSource = RemoteCategoryDataSource()
    private val categoryRepositoryImpl = CategoryRepositoryImpl(remoteCategoryDataSource)
    private lateinit var categoryViewModel: com.example.coffeeshop.ui.admin.category.CategoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewModel()
        setupAddCategory()
        setupBtnBack()
    }

    private fun setupBtnBack() {
        binding.toolbarAddCategory.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    private fun setupAddCategory() {
        binding.btnAddNew.setOnClickListener {
            if (binding.edtCategory.text.toString().isEmpty()) {
                binding.edtCategory.error = "Thể loại không được để trống"
            } else {
                val categoryName = binding.edtCategory.text.toString()
                val category = Category(categoryName)
                categoryViewModel.addCategory(category){isSuccess->
                    if(isSuccess){
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        val factory =
            com.example.coffeeshop.ui.admin.category.CategoryViewModel.Factory(categoryRepositoryImpl)
        categoryViewModel = ViewModelProvider(this, factory)[com.example.coffeeshop.ui.admin.category.CategoryViewModel::class.java]
    }
}