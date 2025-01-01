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
import com.bumptech.glide.Glide
import com.example.coffeeshop.data.model.Drink
import com.example.coffeeshop.data.remote.RemoteCategoryDataSource
import com.example.coffeeshop.data.remote.RemoteDrinkDataSource
import com.example.coffeeshop.data.repository.category.CategoryRepositoryImpl
import com.example.coffeeshop.data.repository.drink.DrinkRepositoryImpl
import com.example.coffeeshop.databinding.ActivityEditDrinkBinding
import com.example.coffeeshop.ui.admin.category.CategoryViewModel
import com.example.coffeeshop.utils.Utils.uploadImage


class EditDrinkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDrinkBinding
    private var imageUri: Uri? = null
    private var imageUrl = ""
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
        binding = ActivityEditDrinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        setupBtnAddImage()
        setupSpinner()
        setupEditDrink()
        getDrinkBeforeEdit()
        setupBack()
    }

    private fun setupBack() {
        binding.toolbarEditDrink.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getDrinkBeforeEdit() {
        val drinkId = intent.getStringExtra("drinkId")
        if (drinkId != null) {
            drinkViewModel.getDrinkById(drinkId) { drink ->
                if (drink != null) {
                    imageUrl = drink.image
                    binding.edtName.setText(drink.name)
                    binding.edtDescription.setText(drink.description)
                    binding.edtPrice.setText(drink.price.toString())
                    binding.edtDiscount.setText(drink.discount.toString())
                    binding.checkOutstanding.isChecked = drink.outstanding
                    if(!isDestroyed&&!isFinishing){
                        Glide.with(this).load(drink.image).into(binding.imgDrink)
                    }
                }
            }
        }

    }

    private fun setupEditDrink() {
        binding.btnAddNew.setOnClickListener {
            if (selectedCategoryId == null) {
                Toast.makeText(this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show()
            } else if (binding.edtName.text.toString().isEmpty()) {
                binding.edtName.error = "Vui lòng nhập tên sản phẩm"
            } else if (binding.edtDescription.text.toString().isEmpty()) {
                binding.edtDescription.error = "Vui lòng nhập mô tả sản phẩm"
            } else if (binding.edtPrice.text.toString().isEmpty()) {
                binding.edtPrice.error = "Vui lòng nhập giá sản phẩm"
            } else if (binding.edtDiscount.text.toString().isEmpty()) {
                binding.edtDiscount.error = "Vui lòng nhập giá khuyến mãi"
            } else {
                val drinkId = intent.getStringExtra("drinkId")
                val name = binding.edtName.text.toString()
                val description = binding.edtDescription.text.toString()
                val price = binding.edtPrice.text.toString()
                val discount = binding.edtDiscount.text.toString()
                val isOutstanding = binding.checkOutstanding.isChecked
                if (imageUri != null) {
                    uploadImage(imageUri!!, "Drink", ProgressDialog(this)) { it ->
                        if (it != null) {
                            val drink = Drink(
                                id = drinkId!!,
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
                }else{
                    val drink = Drink(
                        id = drinkId!!,
                        name = name,
                        description = description,
                        category = selectedCategoryId!!,
                        price = price.toInt(),
                        discount = discount.toInt(),
                        image = imageUrl,
                        outstanding = isOutstanding
                    )
                    saveDrink(drink)
                }
            }

        }
    }


    private fun setupSpinner() {
        selectedCategoryId = intent.getStringExtra("categoryId")
        categoryViewModel.categories.observe(this) { categories ->
            if (categories != null) {
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    categories.map { it.name }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                binding.spinnerCategory.adapter = adapter // Gán adapter trước

                val selectedIndex = categories.indexOfFirst { it.id == selectedCategoryId }
                if (selectedIndex != -1) {
                    binding.spinnerCategory.setSelection(selectedIndex) // Gọi sau khi adapter đã gán
                }

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
        drinkViewModel.editDrink(drink) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Chỉnh sửa sản phẩm thành công", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Chỉnh sửa phẩm thất bại", Toast.LENGTH_SHORT).show()
            }
        }

    }
}