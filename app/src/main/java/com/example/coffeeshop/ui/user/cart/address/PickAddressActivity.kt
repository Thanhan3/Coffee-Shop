package com.example.coffeeshop.ui.user.cart.address

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.R
import com.example.coffeeshop.data.local.AppDatabase
import com.example.coffeeshop.data.model.Address
import com.example.coffeeshop.data.remote.RemoteAddressDataSource
import com.example.coffeeshop.data.remote.RemoteDiscountDataSource
import com.example.coffeeshop.data.repository.address.AddressRepositoryImpl
import com.example.coffeeshop.data.repository.discount.DiscountRepositoryImpl
import com.example.coffeeshop.databinding.ActivityPickAddressBinding
import com.example.coffeeshop.ui.user.cart.CartViewModel

class PickAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPickAddressBinding
    private val remoteDiscountDataSource = RemoteDiscountDataSource()
    private val discountRepositoryImpl = DiscountRepositoryImpl(remoteDiscountDataSource)
    private val remoteAddressDataSource = RemoteAddressDataSource()
    private val addressRepositoryImpl = AddressRepositoryImpl(remoteAddressDataSource)
    private lateinit var cartViewModel: CartViewModel
    private lateinit var appDatabase: AppDatabase
    private lateinit var addressAdapter: AddressAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPickAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupBtnAdd()
        setupViewModel()
        setupView()
        setupBack()
    }

    private fun setupBack() {
        binding.toolbarAddress.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupView() {
        addressAdapter = AddressAdapter(
            object : OnAddressClickListener {
                override fun onAddressClick(address: Address) {
                    cartViewModel.updateAddressDefault(address)
                }
            }
        )
        binding.rvAddress.adapter = addressAdapter
        cartViewModel.addresses.observe(this) { addresses ->
            addressAdapter.setData(addresses)
        }
    }

    private fun setupViewModel() {
        appDatabase = AppDatabase.getInstance(this)
        val cartDao = appDatabase.cartDao()
        cartViewModel = ViewModelProvider(
            this,
            CartViewModel.Factory(cartDao, discountRepositoryImpl, addressRepositoryImpl)
        )[CartViewModel::class.java]
    }

    private fun setupBtnAdd() {
        binding.btnAdd.setOnClickListener {
            val bottomSheetFragment = AddAddressFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }


}