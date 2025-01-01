package com.example.coffeeshop.ui.admin.setting.discount

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
import com.example.coffeeshop.databinding.ActivityAddDiscountBinding

class AddDiscountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDiscountBinding
    private val remoteDiscountDataSource = RemoteDiscountDataSource()
    private val discountRepositoryImpl = DiscountRepositoryImpl(remoteDiscountDataSource)
    private lateinit var viewModel: DiscountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddDiscountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewModel()
        setupBack()
        setupAddTopping()
    }

    private fun setupBack() {
        binding.toolbarAddDiscount.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            DiscountViewModel.Factory(discountRepositoryImpl)
        )[DiscountViewModel::class.java]

    }

    private fun setupAddTopping() {
        binding.btnAddNew.setOnClickListener {
            if (binding.edtDiscountPercent.text.toString().isEmpty()) {
                binding.edtDiscountPercent.error = "Topping không được để trống"
            } else if (binding.edtMinPrice.text.toString().isEmpty()) {
                binding.edtMinPrice.error = "Giá không được để trống"
            } else {
                val discountPercent = binding.edtDiscountPercent.text.toString().toInt()
                val minPrice = binding.edtMinPrice.text.toString().toInt()
                val discount = Discount(
                    discountPercent = discountPercent,
                    minPrice = minPrice
                )
                viewModel.addDiscount(discount){ isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}