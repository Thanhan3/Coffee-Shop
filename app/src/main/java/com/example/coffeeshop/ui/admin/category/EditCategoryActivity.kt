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
import com.example.coffeeshop.databinding.ActivityEditCategoryBinding

class EditCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCategoryBinding
    private val remoteCategoryDataSource = RemoteCategoryDataSource()
    private val categoryRepositoryImpl = CategoryRepositoryImpl(remoteCategoryDataSource)
    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewModel()
        setupEditCategory()
        setupView()
        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbarEditCategory.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    private fun setupView() {
        binding.edtCategory.setText(intent.getStringExtra("categoryName"))
    }

    private fun setupEditCategory() {
        binding.btnEdit.setOnClickListener {
            val categoryName = binding.edtCategory.text.toString()
            val categoryId = intent.getStringExtra("categoryId")
            if (categoryName.isEmpty() || categoryId.isNullOrEmpty()) {
                Toast.makeText(this, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val category = Category(categoryName, categoryId)
            categoryViewModel.editCategory(category){
                if (it){
                    Toast.makeText(this, "Sửa thành công", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }else{
                    Toast.makeText(this, "Sửa thất bại", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    private fun setupViewModel() {
        val factory = CategoryViewModel.Factory(categoryRepositoryImpl)
        categoryViewModel = ViewModelProvider(this, factory)[CategoryViewModel::class.java]
    }
}