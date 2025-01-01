package com.example.coffeeshop.ui.admin.category

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.Drink
import com.example.coffeeshop.data.remote.RemoteDrinkDataSource
import com.example.coffeeshop.data.repository.drink.DrinkRepositoryImpl
import com.example.coffeeshop.databinding.ActivityDetailCategoryBinding
import com.example.coffeeshop.ui.admin.drink.DrinkAdapter
import com.example.coffeeshop.ui.admin.drink.DrinkViewModel
import com.example.coffeeshop.ui.admin.drink.onDrinkDeleteClickListener
import com.example.coffeeshop.ui.admin.drink.onDrinkEditClickListener

class DetailCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCategoryBinding
    private val remoteDrinkDataSource = RemoteDrinkDataSource()
    private val drinkRepositoryImpl = DrinkRepositoryImpl(remoteDrinkDataSource)
    private lateinit var drinkViewModel: DrinkViewModel
    private lateinit var drinkAdapter: DrinkAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCategoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        setupViewModel()
        setupView()

    }

    private fun setupViewModel() {
        val drinkFactory = DrinkViewModel.Factory(drinkRepositoryImpl)
        drinkViewModel = ViewModelProvider(this, drinkFactory)[DrinkViewModel::class.java]
        drinkViewModel.loadDrinkByCategory(intent.getStringExtra("categoryId")!!)
        drinkViewModel.drinkByCategory.observe(this) { drinks ->
            drinkAdapter.updateData(drinks)
        }
    }

    private fun setupView() {
        drinkAdapter = DrinkAdapter(
            object : onDrinkDeleteClickListener {
                override fun onDeleteClick(drink: Drink) {

                }
            },
            object : onDrinkEditClickListener {
                override fun onEditClick(drink: Drink) {

                }
            }
        )
        binding.rvDink.adapter = drinkAdapter
    }

    private fun setupToolbar() {
        binding.toolbarDetailCategory.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.textDetailCategory.text = intent.getStringExtra("categoryName")
    }
}