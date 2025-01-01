package com.example.coffeeshop.ui.admin.drink

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.model.Drink
import com.example.coffeeshop.data.remote.RemoteCategoryDataSource
import com.example.coffeeshop.data.remote.RemoteDrinkDataSource
import com.example.coffeeshop.data.repository.category.CategoryRepositoryImpl
import com.example.coffeeshop.data.repository.drink.DrinkRepositoryImpl
import com.example.coffeeshop.databinding.ActivityAddDrinkBinding
import com.example.coffeeshop.ui.admin.category.CategoryViewModel
import com.example.coffeeshop.utils.Utils.uploadImage

class AddDrinkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDrinkBinding
    private var imageUri: Uri? = null
    private var selectedCategoryId: String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding.imgDrink.setImageURI(it)
            imageUri = it
        }
    }
    private val remoteCategoryDataSource = RemoteCategoryDataSource()
    private val categoryRepositoryImpl = CategoryRepositoryImpl(remoteCategoryDataSource)
    private lateinit var categoryViewModel: CategoryViewModel

    private val remoteDrinkDataSource = RemoteDrinkDataSource()
    private val drinkRepositoryImpl = DrinkRepositoryImpl(remoteDrinkDataSource)
    private lateinit var drinkViewModel: DrinkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddDrinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBtnAddImage()
        setupViewModel()
        setupSpinner()
        setupAddDrink()
        setupBack()
    }

    private fun setupBack() {
        binding.toolbarAddDrink.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupAddDrink() {
        binding.btnAddNew.setOnClickListener {
            if (imageUri == null) {
                Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show()
            } else if(selectedCategoryId == null) {
                Toast.makeText(this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show()
            } else if(binding.edtName.text.toString().isEmpty()){
                binding.edtName.error = "Vui lòng nhập tên sản phẩm"
            } else if(binding.edtDescription.text.toString().isEmpty()){
                binding.edtDescription.error = "Vui lòng nhập mô tả sản phẩm"
            } else if(binding.edtPrice.text.toString().isEmpty()){
                binding.edtPrice.error = "Vui lòng nhập giá sản phẩm"
            } else if(binding.edtDiscount.text.toString().isEmpty()){
                binding.edtDiscount.error = "Vui lòng nhập giá khuyến mãi"
            } else {
                val name = binding.edtName.text.toString()
                val description = binding.edtDescription.text.toString()
                val price = binding.edtPrice.text.toString()
                val discount = binding.edtDiscount.text.toString()
                val isOutstanding = binding.checkOutstanding.isChecked
                uploadImage(imageUri!!, "Drink", ProgressDialog(this)) { it ->
                    if (it != null) {
                        val drink = Drink(
                            id = "",
                            name = name,
                            description = description,
                            category = selectedCategoryId!!,
                            price = price.toInt(),
                            discount = discount.toInt(),
                            image = it,
                            outstanding = isOutstanding
                        )
                        saveDrink(drink)
                    }
                }
            }

        }
    }

    private fun setupSpinner() {
        categoryViewModel.categories.observe(this) { categories ->
            if (categories != null) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    categories.map { it.name }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                binding.spinnerCategory.adapter = adapter

                binding.spinnerCategory.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            selectedCategoryId = categories[position].id
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
            }
        }
    }

    private fun setupBtnAddImage() {
        binding.layoutChoseImage.setOnClickListener {
            launcher.launch("image/*")
        }
    }

    private fun setupViewModel() {

        val drinkFactory = DrinkViewModel.Factory(drinkRepositoryImpl)
        drinkViewModel = ViewModelProvider(this, drinkFactory)[DrinkViewModel::class.java]

        val factory = CategoryViewModel.Factory(categoryRepositoryImpl)
        categoryViewModel = ViewModelProvider(this, factory)[CategoryViewModel::class.java]
    }
    private fun saveDrink(drink: Drink) {
        drinkViewModel.addDrink(drink){isSuccess->
            if(isSuccess){
                Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show()
            }
        }

    }

}