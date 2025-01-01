package com.example.coffeeshop.ui.admin.setting.topping

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.R
import com.example.coffeeshop.data.remote.RemoteToppingDataSource
import com.example.coffeeshop.data.repository.topping.ToppingRepositoryImpl
import com.example.coffeeshop.databinding.ActivityToppingBinding

class ToppingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityToppingBinding
    private val remoteToppingDataSource = RemoteToppingDataSource()
    private val toppingRepositoryImpl = ToppingRepositoryImpl(remoteToppingDataSource)
    private lateinit var toppingViewModel: ToppingViewModel
    private val toppingAdapter = ToppingAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityToppingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupButtonBack()
        setupButtonAdd()
        setupViewModel()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvTopping.adapter = toppingAdapter
        toppingViewModel.toppings.observe(this) { toppings ->
            toppingAdapter.setData(toppings)
        }
    }

    private fun setupButtonAdd() {
        binding.fabAddTopping.setOnClickListener {
            val intent = Intent(this, AddToppingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupButtonBack() {
        binding.toolbarTopping.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    private fun setupViewModel() {
        toppingViewModel = ViewModelProvider(
            this,
            ToppingViewModel.Factory(toppingRepositoryImpl)
        )[ToppingViewModel::class.java]
    }
}