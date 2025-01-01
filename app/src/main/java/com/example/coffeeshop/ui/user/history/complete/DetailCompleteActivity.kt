package com.example.coffeeshop.ui.user.history.complete

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.R
import com.example.coffeeshop.data.FirebaseCallback
import com.example.coffeeshop.data.model.CartItem
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.model.Rate
import com.example.coffeeshop.data.remote.RemoteOrderDataSource
import com.example.coffeeshop.data.repository.order.OrderRepositoryImpl
import com.example.coffeeshop.databinding.ActivityDetailCompleteBinding
import com.example.coffeeshop.utils.Constant
import com.example.coffeeshop.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailCompleteActivity : AppCompatActivity() {
    private lateinit var adapter: DetailAdapter
    private lateinit var binding: ActivityDetailCompleteBinding
    private val remoteOrderDataSource = RemoteOrderDataSource()
    private val orderRepository = OrderRepositoryImpl(remoteOrderDataSource)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val orderId = intent.getStringExtra("orderId")
        getOrder(orderId)
        setupBack()
    }

    private fun getRole(callback: (Int) -> Unit) {
        Utils.getRoleUser {
            callback(it)
        }
    }

    private fun setupBack() {
        binding.toolbarDetailOrder.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getOrder(orderId: String?) {
        orderRepository.getOderById(orderId!!, object : FirebaseCallback<Order> {
            override fun onSuccess(data: Order) {
                setupRecycler(data)
                setupLayoutPayment(data)
                setupLayoutAddress(data)
                setupLayoutInfor(data)
            }

            override fun onFailure(error: String) {

            }
        })
    }

    private fun setupLayoutAddress(data: Order) {
        val address = data.address
        if (address != null) {
            binding.textAddress.text = address.address
            binding.textName.text = address.receiverName
            binding.textPhoneNumber.text = address.numberPhone
        }
    }

    private fun setupLayoutInfor(data: Order) {
        binding.textOrderId.text = data.orderId
        binding.textTime.text = Utils.convertMillisToDate(data.timestamp)
    }

    @SuppressLint("SetTextI18n")
    private fun setupLayoutPayment(data: Order) {
        binding.textPrice.text = data.price.toString().plus(".000vnd")
        val discount = data.discount
        if (discount != null) {
            val priceDiscount = "${(data.price) * discount.discountPercent / 100}.000vnd"
            binding.textDiscount.text = "-$priceDiscount"
            binding.textFinalPrice.text =
                (data.price - ((data.price) * discount.discountPercent / 100)).toString()
                    .plus(".000vnd")
        } else {
            binding.textFinalPrice.text = data.price.toString().plus(".000vnd")
            binding.textDiscount.text = "-0.000vnd"
        }

        val paymentMethod = data.paymentMethod
        if (paymentMethod == Constant.PAYMENT_COD) {
            binding.textPaymentMethod.text = "Tiền mặt"
        } else if (paymentMethod == Constant.PAYMENT_BANK) {
            binding.textPaymentMethod.text = "Chuyển khoản"
        }
    }

    private fun setupRecycler(data: Order) {
        adapter = DetailAdapter(object : OnRateClickListener {
            override fun onRateClick(cartItem: CartItem) {
                getRole {
                    if (it == Constant.USER) {
                        val intent =
                            Intent(this@DetailCompleteActivity, UserRateActivity::class.java)
                        intent.putExtra("drinkId", cartItem.drink.id)
                        intent.putExtra("orderId", data.orderId)
                        startActivity(intent)
                    }
                }
            }

            override fun getRate(idDrink: String, callback: (Rate?) -> Unit) {
                val ref = FirebaseDatabase.getInstance().reference.child("Rates").child(idDrink!!)
                ref.addValueEventListener(object : ValueEventListener {
                    var rate: Rate? = null
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children) {
                            val rate = ds.getValue(Rate::class.java)
                            if (rate != null) {
                                if (rate.idOrder == data.orderId
                                ) {
                                    this.rate = rate
                                }
                            }
                        }
                        callback(this.rate)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback(null)
                    }

                })
            }

        })
        binding.recyclerDetailOrder.adapter = adapter
        adapter.setData(data.items)
    }
}