package com.example.coffeeshop.ui.user.cart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.R
import com.example.coffeeshop.data.local.AppDatabase
import com.example.coffeeshop.data.model.Address
import com.example.coffeeshop.data.model.CartItem
import com.example.coffeeshop.data.model.Discount
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.remote.RemoteAddressDataSource
import com.example.coffeeshop.data.remote.RemoteDiscountDataSource
import com.example.coffeeshop.data.repository.address.AddressRepositoryImpl
import com.example.coffeeshop.data.repository.discount.DiscountRepositoryImpl
import com.example.coffeeshop.databinding.ActivityDetailCartBinding
import com.example.coffeeshop.ui.user.cart.address.PickAddressActivity
import com.example.coffeeshop.ui.user.cart.discount.PickDiscountActivity
import com.example.coffeeshop.ui.user.cart.payment.PaymentMethodActivity
import com.example.coffeeshop.utils.Constant.PAYMENT_BANK
import com.example.coffeeshop.utils.Constant.PAYMENT_COD
import com.example.coffeeshop.utils.Constant.PAYMENT_METHODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailCartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var appDatabase: AppDatabase
    private lateinit var cartItemAdapter: CartItemAdapter
    private val remoteDiscountDataSource = RemoteDiscountDataSource()
    private val discountRepositoryImpl = DiscountRepositoryImpl(remoteDiscountDataSource)
    private val remoteAddressDataSource = RemoteAddressDataSource()
    private val addressRepositoryImpl = AddressRepositoryImpl(remoteAddressDataSource)
    private var discount: Discount? = null
    private var address: Address? = null
    private var paymentMethod = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewModel()
        setupView()
        setupDiscount()
        setupAddress()
        setupBack()
        setupPayment()
        setupBack()
        setupBtnOrder()
    }

    private fun setupBtnOrder() {
        binding.btnOrder.setOnClickListener {
            if (address == null) {
                Toast.makeText(this, "Vui lòng chọn địa chỉ giao hàng", Toast.LENGTH_SHORT).show()
            } else if (paymentMethod == 0) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT)
                    .show()
            } else {
                var oderCartItems = mutableListOf<CartItem>()
                var totalPrice = 0
                cartViewModel.cartItems.observe(this) { cartItems ->
                    cartViewModel.totalPrice.observe(this) { it ->
                        oderCartItems = cartItems as MutableList<CartItem>
                        totalPrice = it
                    }
                }
                val finalPrice = totalPrice * (100-(discount?.discountPercent ?: 0)) / 100
                val order = Order(
                    address = address!!,
                    items = oderCartItems,
                    price = totalPrice,
                    discount = discount,
                    paymentMethod = paymentMethod,
                    userId = FirebaseAuth.getInstance().currentUser!!.uid,
                    finalPrice = finalPrice
                )
                val ref = FirebaseDatabase.getInstance().reference.child("Orders")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                val key = System.currentTimeMillis().toString()
                val orderWithId = order.copy(orderId = key)
                ref.child(key).setValue(orderWithId)
                    .addOnSuccessListener {
                        cartViewModel.deleteAllCartItem()
                        Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ConfirmActivity::class.java)
                        intent.putExtra("order", orderWithId)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun setupBack() {
        binding.toolbarDetailCart.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupAddress() {
        binding.layoutAddress.setOnClickListener {
            val intent = Intent(this, PickAddressActivity::class.java)
            startActivity(intent)
        }
        cartViewModel.getAddressDefault { address ->
            if (address != null) {
                binding.textAddress.text = address.address
                this.address = address
                Log.d("DetailCartActivity", "Address: ${address.address}")
            } else {
                binding.textAddress.text = "Chưa có địa chỉ"
            }
        }
    }

    private fun setupDiscount() {
        binding.layoutDiscount.setOnClickListener {
            val intent = Intent(this, PickDiscountActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PICK_DISCOUNT)
        }
    }

    private fun setupPayment() {
        binding.layoutPaymentMethod.setOnClickListener {
            val intent = Intent(this, PaymentMethodActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_PICK_PAYMENT)
        }
    }

    @SuppressLint("SetTextI18n")
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_DISCOUNT && resultCode == RESULT_OK) {
            val discount = data?.getSerializableExtra("selectedDiscount") as? Discount
            discount?.let { discount ->
                binding.textDiscount.text = "Giảm giá ${discount.discountPercent}%"
                binding.textNameDiscount.text = "Giảm giá ${discount.discountPercent}%"
                cartViewModel.totalPrice.observe(this) {
                    if (it < discount.minPrice) {
                        binding.textDiscount.text = "Không đủ điều kiện"
                        binding.textNameDiscount.text = "Không đủ điều kiện"
                        binding.textFinalDiscount.text = "-0.000vnd"
                        binding.textFinalPrice.text = "$it.000vnd"
                        this.discount = null
                    } else {
                        binding.textDiscount.text = "Giảm giá ${discount.discountPercent}%"
                        binding.textNameDiscount.text = "Giảm giá ${discount.discountPercent}%"
                        val discountPrice = it * discount.discountPercent / 100
                        binding.textFinalDiscount.text = "-$discountPrice.000vnd"
                        binding.textFinalPrice.text = "${it - discountPrice}.000vnd"
                        this.discount = discount
                    }

                }
            }
        }
        if (requestCode == REQUEST_CODE_PICK_PAYMENT && resultCode == RESULT_OK) {
            val paymentMethod = data?.getIntExtra(PAYMENT_METHODE, 0)
            if (paymentMethod == PAYMENT_COD) {
                binding.textPayment.text = "Thanh toán tiền mặt"
                this.paymentMethod = PAYMENT_COD
            }
            if (paymentMethod == PAYMENT_BANK) {
                binding.textPayment.text = "Thanh toán chuyển khoản"
                this.paymentMethod = PAYMENT_BANK
            }
        }
    }

    private fun setupView() {
        cartItemAdapter = CartItemAdapter(object : OnCartItemChange {
            override fun onCountChange(cartItem: CartItem) {
                cartViewModel.updateCartItem(cartItem)
            }

            override fun onCartItemDelete(cartItem: CartItem) {
                cartViewModel.deleteCartItem(cartItem)
            }
        })
        binding.rvItemCart.adapter = cartItemAdapter
    }


    @SuppressLint("SetTextI18n")
    private fun setupViewModel() {
        appDatabase = AppDatabase.getInstance(this)
        val cartDao = appDatabase.cartDao()
        cartViewModel = ViewModelProvider(
            this,
            CartViewModel.Factory(cartDao, discountRepositoryImpl, addressRepositoryImpl)
        )[CartViewModel::class.java]
        cartViewModel.totalPrice.observe(this) {
            binding.textPriceTotal.text = "$it.000vnd"
            binding.textFinalPrice.text = "$it.000vnd"
        }
        cartViewModel.cartItems.observe(this) { cartItems ->
            binding.textFinalDrinkCount.text = "(${cartItems.size} Đồ uống)"
            cartItemAdapter.setCartItems(cartItems)
        }
    }

    companion object {
        const val REQUEST_CODE_PICK_DISCOUNT = 1001
        const val REQUEST_CODE_PICK_PAYMENT = 1002
    }
}