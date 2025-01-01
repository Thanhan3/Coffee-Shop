package com.example.coffeeshop.ui.user.cart.discount

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.R
import com.example.coffeeshop.data.local.AppDatabase
import com.example.coffeeshop.data.model.Discount
import com.example.coffeeshop.data.remote.RemoteAddressDataSource
import com.example.coffeeshop.data.remote.RemoteDiscountDataSource
import com.example.coffeeshop.data.repository.address.AddressRepositoryImpl
import com.example.coffeeshop.data.repository.discount.DiscountRepositoryImpl
import com.example.coffeeshop.databinding.ActivityPickDiscountBinding
import com.example.coffeeshop.ui.user.cart.CartViewModel

class PickDiscountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPickDiscountBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var appDatabase: AppDatabase
    private lateinit var discountAdapter: UserDiscountAdapter
    private val remoteDiscountDataSource = RemoteDiscountDataSource()
    private val discountRepositoryImpl = DiscountRepositoryImpl(remoteDiscountDataSource)
    private val remoteAddressDataSource = RemoteAddressDataSource()
    private val addressRepositoryImpl = AddressRepositoryImpl(remoteAddressDataSource)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPickDiscountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewModel()
        setupView()
        setupBack()
    }



    private fun setupBack() {
        binding.toolbarDiscount.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    private fun setupView() {
        discountAdapter = UserDiscountAdapter(
            object : OnDiscountClick {
                override fun onItemDiscountClick(discount: Discount) {
                    val resultIntent = Intent()
                    resultIntent.putExtra("selectedDiscount", discount)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
                override fun isDiscountAvailable(discount: Discount): Boolean {
                    var isAvailable = false
                    cartViewModel.totalPrice.value?.let {
                        if (it >= discount.minPrice) {
                            isAvailable = true
                        }
                    }
                    return isAvailable
                }
            }
        )
        binding.rvDiscount.adapter = discountAdapter
    }


    private fun setupViewModel() {
        appDatabase = AppDatabase.getInstance(this)
        val cartDao = appDatabase.cartDao()
        cartViewModel =
            ViewModelProvider(
                this, CartViewModel.Factory(
                    cartDao, discountRepositoryImpl, addressRepositoryImpl
                )
            )[CartViewModel::class.java]
        cartViewModel.discounts.observe(this) { discounts ->
            discountAdapter.updateData(discounts)
        }

    }
}