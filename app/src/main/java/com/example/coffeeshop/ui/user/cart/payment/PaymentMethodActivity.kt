package com.example.coffeeshop.ui.user.cart.payment

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.R
import com.example.coffeeshop.databinding.ActivityPaymentMethodBinding
import com.example.coffeeshop.utils.Constant.PAYMENT_BANK
import com.example.coffeeshop.utils.Constant.PAYMENT_COD
import com.example.coffeeshop.utils.Constant.PAYMENT_METHODE

class PaymentMethodActivity : AppCompatActivity() {

    private var selectedPaymentMethod = 0
    private lateinit var binding: ActivityPaymentMethodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupCheckBox()
        setupBack()
    }

    private fun setupBack() {
        binding.toolbarPaymentMethod.setNavigationOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(PAYMENT_METHODE, selectedPaymentMethod)
            setResult(RESULT_OK, resultIntent)
            super.onBackPressed()
        }
    }

    private fun setupCheckBox() {
        binding.checkCod.setOnClickListener {
            if (binding.checkCod.isChecked) {
                binding.checkBank.isChecked = false
                selectedPaymentMethod = PAYMENT_COD
            }
        }
        binding.checkBank.setOnClickListener {
            if (binding.checkBank.isChecked) {
                binding.checkCod.isChecked = false
                selectedPaymentMethod = PAYMENT_BANK
            }
        }
    }
}