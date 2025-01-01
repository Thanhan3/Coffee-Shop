package com.example.coffeeshop.ui.admin.setting.discount

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.Discount
import com.example.coffeeshop.data.remote.RemoteDiscountDataSource
import com.example.coffeeshop.data.repository.discount.DiscountRepositoryImpl
import com.example.coffeeshop.databinding.ActivityDiscountBinding

class DiscountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiscountBinding
    private val remoteDiscountDataSource = RemoteDiscountDataSource()
    private val discountRepositoryImpl = DiscountRepositoryImpl(remoteDiscountDataSource)
    private lateinit var discountAdapter: DiscountAdapter
    private lateinit var viewModel: DiscountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDiscountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewModel()
        setupButtonAdd()
        setupRecyclerView()
        setupBack()
    }

    private fun setupBack() {
        binding.toolbarDiscount.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    private fun setupButtonAdd() {
        binding.fabAddDiscount.setOnClickListener {
            val intent = Intent(this, AddDiscountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        discountAdapter = DiscountAdapter(
            object : OnItemClickListener {
                override fun onEditClick(discount: Discount) {

                }

                override fun onDeleteClick(discount: Discount) {
                    viewModel.deleteDiscount(discount){
                        if (it){
                            Toast.makeText(this@DiscountActivity, "Xóa thành công", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@DiscountActivity, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        )
        binding.rvDiscount.adapter = discountAdapter
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            DiscountViewModel.Factory(discountRepositoryImpl)
        )[DiscountViewModel::class.java]
        viewModel.discounts.observe(this) { discounts ->
            discountAdapter.setData(discounts)
        }
    }
}