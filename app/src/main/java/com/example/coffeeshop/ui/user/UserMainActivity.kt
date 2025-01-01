package com.example.coffeeshop.ui.user

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.coffeeshop.R
import com.example.coffeeshop.data.local.AppDatabase
import com.example.coffeeshop.data.remote.RemoteAddressDataSource
import com.example.coffeeshop.data.remote.RemoteDiscountDataSource
import com.example.coffeeshop.data.repository.address.AddressRepositoryImpl
import com.example.coffeeshop.data.repository.discount.DiscountRepositoryImpl
import com.example.coffeeshop.databinding.ActivityUserMainBinding
import com.example.coffeeshop.ui.user.cart.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserMainActivity : AppCompatActivity() {
    private lateinit var appDatabase: AppDatabase
    private lateinit var binding: ActivityUserMainBinding
    private lateinit var cartViewModel: CartViewModel
    private val remoteDiscountDataSource = RemoteDiscountDataSource()
    private val discountRepositoryImpl = DiscountRepositoryImpl(remoteDiscountDataSource)
    private val remoteAddressDataSource = RemoteAddressDataSource()
    private val addressRepositoryImpl = AddressRepositoryImpl(remoteAddressDataSource)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_user_main)
            ?.findNavController()
        setupBottomNavigation(navController!!)
        setUpMiniCart()
        setupViewModel()
    }

    private fun setupViewModel() {
        appDatabase = AppDatabase.getInstance(applicationContext)
        val cartDao = appDatabase.cartDao()
        cartViewModel =
            ViewModelProvider(this, CartViewModel.Factory(
                cartDao, discountRepositoryImpl,addressRepositoryImpl)
            )[CartViewModel::class.java]
        cartViewModel.cartItems.observe(this) { cartItems ->
            if (cartItems.isNotEmpty()) {
                binding.fcvMiniCart.visibility = View.VISIBLE
            } else {
                binding.fcvMiniCart.visibility = View.GONE
            }
        }

    }

    private fun setUpMiniCart() {

    }

    private fun setupBottomNavigation(navController: NavController) {
        val navView: BottomNavigationView = binding.navView
        navView.setupWithNavController(navController)
    }
}