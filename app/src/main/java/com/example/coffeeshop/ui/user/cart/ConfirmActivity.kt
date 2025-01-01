package com.example.coffeeshop.ui.user.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.databinding.ActivityConfirmBinding
import com.example.coffeeshop.ui.user.history.follow_oder.FollowOrderActivity
import com.example.coffeeshop.utils.Constant
import java.text.SimpleDateFormat
import java.util.Date

class ConfirmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmBinding
    private val confirmAdapter = ConfirmAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val order = intent.getParcelableExtra<Order>("order")
        if (order != null) {
            setupFollowOrder(order)
        }

        setupLayoutTop(order)
        setupLayoutPayment(order)
        setupLayoutDrink(order)
        setupLayoutAddress(order)
        setupBack()
    }

    private fun setupFollowOrder(order: Order) {
        binding.btnFollowOrder.setOnClickListener {
            val intent = Intent(this, FollowOrderActivity::class.java)
            intent.putExtra("orderId", order.orderId)
            startActivity(intent)
            finish()
        }
    }

    private fun setupLayoutAddress(order: Order?) {
        val address = order!!.address
        if (address != null) {
            binding.textAddress.text = address.address
            binding.textName.text = address.receiverName
            binding.textPhoneNumber.text = address.numberPhone
        }
    }

    private fun setupBack() {
        binding.toolbarReceipt.setNavigationOnClickListener {
            this.finish()
        }
    }

    private fun setupLayoutDrink(order: Order?) {
        binding.recyclerReceipt.adapter = confirmAdapter
        confirmAdapter.submitList(order!!.items)
    }

    @SuppressLint("SetTextI18n")
    private fun setupLayoutPayment(order: Order?) {
        val discount = order!!.discount
        if (discount != null) {
            val finalPrice = order.price - (discount.discountPercent * order.price / 100)
            binding.textFinalPrice.text = "$finalPrice.000vnd"
            binding.textDiscount.text = "-${(discount.discountPercent * order.price / 100)}.000vnd"
        } else {
            binding.textDiscount.text = "-.000vnd"
            binding.textFinalPrice.text = order.price.toString() + ".000vnd"
        }
        binding.textPrice.text = order.price.toString() + ".000vnd"
        val paymentMethod = order.paymentMethod
        if (paymentMethod == Constant.PAYMENT_COD) {
            binding.textPaymentMethod.text = "Tiền mặt"
        } else {
            binding.textPaymentMethod.text = "Chuyển khoản"
        }
    }

    private fun setupLayoutTop(order: Order?) {
        binding.textOrderId.text = order!!.orderId
        binding.textTime.text = convertMillisToDate(order.timestamp)
    }


    @SuppressLint("SimpleDateFormat")
    fun convertMillisToDate(millis: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date = Date(millis)
        return dateFormat.format(date)
    }
}