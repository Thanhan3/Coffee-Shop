package com.example.coffeeshop.ui.user.history.follow_oder

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.R
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.remote.RemoteOrderDataSource
import com.example.coffeeshop.data.repository.order.OrderRepositoryImpl
import com.example.coffeeshop.databinding.ActivityFollowOrderBinding
import com.example.coffeeshop.ui.user.cart.ConfirmActivity
import com.example.coffeeshop.ui.user.cart.ConfirmAdapter
import com.example.coffeeshop.utils.Constant
import com.example.coffeeshop.utils.Utils

class FollowOrderActivity : AppCompatActivity() {

    private var adpter = ConfirmAdapter()
    private val remoteOrderDataSource = RemoteOrderDataSource()
    private val orderRepository = OrderRepositoryImpl(remoteOrderDataSource)
    private lateinit var binding: ActivityFollowOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFollowOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val orderId = intent.getStringExtra("orderId")
        getOrder(orderId)
        setupFollowOrderAdmin()
        setupBack()
    }

    private fun setupLayoutReceipt(order: Order) {
        binding.textReceipt.setOnClickListener {
            val intent = Intent(this, ConfirmActivity::class.java)
            intent.putExtra("order", order)
            startActivity(intent)
        }
    }


    private fun setupBack() {
        binding.toolbarFollowOrder.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getOrder(orderId: String?) {
        orderRepository.getOderById(orderId!!, object : FirebaseCallback<Order> {
            override fun onSuccess(data: Order) {
                setupLayoutFollowOrder(data)
                setupRecycler(data)
                setupLayoutReceipt(data)
            }

            override fun onFailure(error: String) {

            }
        })
    }

    private fun setupFollowOrderAdmin() {
        Utils.getRoleUser {
            if (it == Constant.USER) {
                binding.btnReceivedOrder.text = "Hủy đơn hàng"
            } else {
                binding.btnReceivedOrder.text = "Hoàn thành"
            }
        }
    }


    private fun upDateStatus(status: Int, order: Order) {
        orderRepository.updateStatus(status, order) { isSuccess ->
            if (isSuccess) {
                //sendNotification
            }
        }
    }

    private fun updateSuccess(order: Order) {
        orderRepository.updateSuccess(order) { isSuccess ->
            finish()
        }
    }

    private fun updateCancel(order: Order) {
        orderRepository.updateCancel(order) { isSuccess ->
            finish()
        }
    }

    private fun setupLayoutFollowOrder(order: Order) {
        val process = order.shopStatus
        when (process) {
            Constant.SHOP_RECEIVED -> {
                binding.imgShopReceived.setImageResource(R.drawable.ic_checked)
                binding.imgShopPrepare.setImageResource(R.drawable.ic_check)
                binding.imgShopComplete.setImageResource(R.drawable.ic_check)
            }

            Constant.SHOP_PREPARING -> {
                binding.imgShopReceived.setImageResource(R.drawable.ic_checked)
                binding.imgShopPrepare.setImageResource(R.drawable.ic_checked)
                binding.imgShopComplete.setImageResource(R.drawable.ic_check)
            }

            Constant.SHOP_SHIPPING -> {
                binding.imgShopReceived.setImageResource(R.drawable.ic_checked)
                binding.imgShopPrepare.setImageResource(R.drawable.ic_checked)
                binding.imgShopComplete.setImageResource(R.drawable.ic_checked)
            }
        }

        Utils.getRoleUser {
            if (it == Constant.ADMIN) {
                when (process) {
                    Constant.SHOP_RECEIVED -> {
                        binding.btnReceivedOrder.isEnabled = false
                    }

                    Constant.SHOP_PREPARING -> {
                        binding.btnReceivedOrder.isEnabled = false
                    }

                    Constant.SHOP_SHIPPING -> {
                        binding.btnReceivedOrder.isEnabled = true
                    }
                }
            } else {
                when (process) {
                    Constant.SHOP_RECEIVED -> {
                        binding.btnReceivedOrder.isEnabled = true
                    }

                    Constant.SHOP_PREPARING -> {
                        binding.btnReceivedOrder.isEnabled = true
                    }

                    Constant.SHOP_SHIPPING -> {
                        binding.btnReceivedOrder.isEnabled = false
                    }
                }
            }
        }

        binding.btnReceivedOrder.setOnClickListener {
            Utils.getRoleUser {
                if (it == Constant.ADMIN) {
                    updateSuccess(order)
                } else {
                    updateCancel(order)
                }
            }
        }
        Utils.getRoleUser {
            if (it == Constant.ADMIN) {
                binding.imgShopReceived.setOnClickListener {
                    upDateStatus(Constant.SHOP_RECEIVED, order)
                }
                binding.imgShopPrepare.setOnClickListener {
                    upDateStatus(Constant.SHOP_PREPARING, order)
                }
                binding.imgShopComplete.setOnClickListener {
                    upDateStatus(Constant.SHOP_SHIPPING, order)
                }
            }
        }

    }

    private fun setupRecycler(order: Order) {
        binding.recyclerFollowOrder.adapter = adpter
        adpter.submitList(order.items)
    }


}

