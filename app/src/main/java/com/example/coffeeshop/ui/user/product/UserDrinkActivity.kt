package com.example.coffeeshop.ui.user.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.coffeeshop.R
import com.example.coffeeshop.data.local.AppDatabase
import com.example.coffeeshop.data.model.CartItem
import com.example.coffeeshop.data.model.Drink
import com.example.coffeeshop.data.model.Rate
import com.example.coffeeshop.data.model.Topping
import com.example.coffeeshop.data.remote.RemoteDrinkDataSource
import com.example.coffeeshop.data.remote.RemoteToppingDataSource
import com.example.coffeeshop.data.repository.drink.DrinkRepositoryImpl
import com.example.coffeeshop.data.repository.topping.ToppingRepositoryImpl
import com.example.coffeeshop.databinding.ActivityUserDrinkBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserDrinkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDrinkBinding
    private val remoteDrinkDataSource = RemoteDrinkDataSource()
    private val drinkRepositoryImpl = DrinkRepositoryImpl(remoteDrinkDataSource)
    private val remoteToppingDataSource = RemoteToppingDataSource()
    private val toppingRepositoryImpl = ToppingRepositoryImpl(remoteToppingDataSource)
    private lateinit var drinkViewModel: UserDrinkViewModel
    private lateinit var toppingAdapter: ChoseToppingAdapter
    private val selectedTopping = mutableListOf<Topping>()
    private lateinit var appDatabase: AppDatabase
    private var cartItem: CartItem? = null
    private var isHot = false
    private var isLessSugar = false
    private var isLessIce = false
    private var itemCount = 1
    private var drink: Drink? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserDrinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewModel()
        setupBack()
        setupLayoutTopping()
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun setupLayoutRate() {
        getRate(drink!!.id) { rates ->
            if (rates.isNotEmpty()) {
                val sumRate = rates.sumOf { it.rate }
                val averageRate = (sumRate.toDouble()/ rates.size)
                val formattedRate = String.format("%.1f", averageRate)
                binding.textRating.text =formattedRate
                binding.textRatingCount.text = "(${rates.size})"
            }else{
                binding.textRating.text = "0"
                binding.textRatingCount.text = "(0)"
            }
        }
    }
    private fun getRate(drinkId: String, callback: (List<Rate>) -> Unit) {
        val ref = FirebaseDatabase.getInstance().reference.child("Rates").child(drinkId)
        ref.addValueEventListener(object : ValueEventListener {
            val listRate = mutableListOf<Rate>()
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val rate = data.getValue(Rate::class.java)
                    if (rate != null) {
                        listRate.add(rate)
                    }
                }
                callback(listRate)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        }
        )
    }

    private fun setupItemCount() {
        binding.textCount.text = itemCount.toString()
    }

    private fun setupView() {
        if (drink != null) {
            Glide.with(this).load(drink!!.image).into(binding.imgDrink)
            binding.textTitle.text = drink!!.name
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupLayoutDrink() {
        val discount = drink!!.discount
        val price = drink!!.price * (100 - discount) / 100

        binding.textNameDrink.text = drink!!.name
        binding.textDescription.text = drink!!.description
        binding.textPrice.text = "$price.000vnd"

        binding.imageAdd.setOnClickListener {
            var count = binding.textCount.text.toString().toInt()
            count++
            binding.textCount.text = count.toString()
            drinkViewModel.updateItemCount(count)
        }
        binding.imageMinus.setOnClickListener {
            var count = binding.textCount.text.toString().toInt()
            if (count > 1) {
                count--
                binding.textCount.text = count.toString()
                drinkViewModel.updateItemCount(count)
            }
        }
    }

    private fun setupLayoutCustom() {
        if (isHot) {
            binding.textHot.setBackgroundResource(R.drawable.square_selected_background)
            binding.textHot.setTextColor(getColor(R.color.white))
            binding.textIce.setBackgroundResource(R.drawable.square_background)
            binding.textIce.setTextColor(getColor(R.color.black))
            binding.layoutIce.visibility = View.GONE
        }
        if (isLessSugar) {
            binding.textLessSugar.setBackgroundResource(R.drawable.square_selected_background)
            binding.textLessSugar.setTextColor(getColor(R.color.white))
            binding.textSugarNormal.setBackgroundResource(R.drawable.square_background)
            binding.textSugarNormal.setTextColor(getColor(R.color.black))
        }
        if (isLessIce) {
            binding.textLessIce.setBackgroundResource(R.drawable.square_selected_background)
            binding.textLessIce.setTextColor(getColor(R.color.white))
            binding.textIceNormal.setBackgroundResource(R.drawable.square_background)
            binding.textIceNormal.setTextColor(getColor(R.color.black))
        }
        binding.textHot.setOnClickListener {
            isHot = true
            binding.textHot.setBackgroundResource(R.drawable.square_selected_background)
            binding.textHot.setTextColor(getColor(R.color.white))
            binding.textIce.setBackgroundResource(R.drawable.square_background)
            binding.textIce.setTextColor(getColor(R.color.black))
            binding.layoutIce.visibility = View.GONE
        }
        binding.textIce.setOnClickListener {
            isHot = false
            binding.layoutIce.visibility = View.VISIBLE
            binding.textIce.setBackgroundResource(R.drawable.square_selected_background)
            binding.textIce.setTextColor(getColor(R.color.white))
            binding.textHot.setBackgroundResource(R.drawable.square_background)
            binding.textHot.setTextColor(getColor(R.color.black))
        }
        binding.textSugarNormal.setOnClickListener {
            isLessSugar = false
            binding.textSugarNormal.setBackgroundResource(R.drawable.square_selected_background)
            binding.textSugarNormal.setTextColor(getColor(R.color.white))
            binding.textLessSugar.setBackgroundResource(R.drawable.square_background)
            binding.textLessSugar.setTextColor(getColor(R.color.black))
        }
        binding.textLessSugar.setOnClickListener {
            isLessSugar = true
            binding.textLessSugar.setBackgroundResource(R.drawable.square_selected_background)
            binding.textLessSugar.setTextColor(getColor(R.color.white))
            binding.textSugarNormal.setBackgroundResource(R.drawable.square_background)
            binding.textSugarNormal.setTextColor(getColor(R.color.black))
        }
        binding.textIceNormal.setOnClickListener {
            isLessIce = false
            binding.textIceNormal.setBackgroundResource(R.drawable.square_selected_background)
            binding.textIceNormal.setTextColor(getColor(R.color.white))
            binding.textLessIce.setBackgroundResource(R.drawable.square_background)
            binding.textLessIce.setTextColor(getColor(R.color.black))
        }
        binding.textLessIce.setOnClickListener {
            isLessIce = true
            binding.textLessIce.setBackgroundResource(R.drawable.square_selected_background)
            binding.textLessIce.setTextColor(getColor(R.color.white))
            binding.textIceNormal.setBackgroundResource(R.drawable.square_background)
            binding.textIceNormal.setTextColor(getColor(R.color.black))
        }

    }

    private fun setupLayoutTopping() {
        toppingAdapter = ChoseToppingAdapter(object : ToppingListener {
            override fun onSelected(topping: Topping) {
                selectedTopping.add(topping)
                drinkViewModel.updateSelectedTopping(selectedTopping)
            }

            override fun onUnSelected(topping: Topping) {
                selectedTopping.remove(topping)
                drinkViewModel.updateSelectedTopping(selectedTopping)
            }
        })
        binding.rvTopping.adapter = toppingAdapter
        drinkViewModel.toppings.observe(this) {
            toppingAdapter.updateTopping(it)
        }
        drinkViewModel.selectedTopping.observe(this) {
            toppingAdapter.updateSelectedTopping(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupLayoutPrice() {
        if(cartItem!= null){
            drinkViewModel.updateTotalPrice(cartItem!!.totalPrice)
            Log.d("cartItem", cartItem!!.totalPrice.toString())
        }
        drinkViewModel.totalPrice.observe(this) {
            val price = it.toString()
            binding.textPriceTotal.text = "$price.000vnd"
        }
    }

    private fun setupBack() {
        binding.toolbarDrink.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    private fun setupBtnAddToCart() {
        if (this.cartItem != null) {
            // Nếu cartItem đã tồn tại, sửa lại giỏ hàng
            binding.btnAddCart.text = "Sửa lại đồ uống"
            binding.btnAddCart.setOnClickListener {
                drinkViewModel.totalPrice.observe(this) { price ->
                    drinkViewModel.itemCount.observe(this){
                        val cartItem = CartItem(
                            cartId = drink!!.id,
                            count = it,
                            topping = selectedTopping,
                            drink = this.drink!!,
                            totalPrice = price,
                            note = binding.edNote.text.toString(),
                            isHot = isHot,
                            isLessSugar = isLessSugar,
                            isLessIce = isLessIce
                        )
                        drinkViewModel.updateCartItem(cartItem)
                    }

                }
                finish()
            }
        } else {
            // Nếu cartItem là null, thêm đồ uống vào giỏ hàng
            binding.btnAddCart.text = "Thêm vào giỏ hàng"
            binding.btnAddCart.setOnClickListener {
                drinkViewModel.totalPrice.observe(this) { price ->
                    drinkViewModel.itemCount.observe(this){
                        val cartItem = CartItem(
                            cartId = drink!!.id,
                            count = it,
                            topping = selectedTopping,
                            drink = this.drink!!,
                            totalPrice = price,
                            note = binding.edNote.text.toString(),
                            isHot = isHot,
                            isLessSugar = isLessSugar,
                            isLessIce = isLessIce
                        )
                        drinkViewModel.addCartItem(cartItem)
                    }
                }
                finish()
            }
        }
    }


    private fun setupViewModel() {
        appDatabase = AppDatabase.getInstance(applicationContext)
        val cartDao = appDatabase.cartDao()
        val drinkFactory = UserDrinkViewModel.Factory(
            drinkRepositoryImpl,
            toppingRepositoryImpl,
            cartDao
        )
        drinkViewModel = ViewModelProvider(this, drinkFactory)[UserDrinkViewModel::class.java]
        drinkViewModel.getDrinkById(intent.getStringExtra("drinkId")!!)
        drinkViewModel.updateItemCount(itemCount)
        drinkViewModel.drink.observe(this) { it ->
            drink = it
            setupItemCount()
            setupView()
            setupLayoutDrink()
            setupLayoutCustom()
            setupLayoutPrice()
            setupBtnAddToCart()
            setupLayoutRate()
            drinkViewModel.calculateTotalPrice()
            drinkViewModel.getCartItemById(drink!!.id).observe(this) { cartItem ->
                if (cartItem != null) {
                    this.cartItem = cartItem
                    isHot = cartItem.isHot
                    isLessSugar = cartItem.isLessSugar
                    isLessIce = cartItem.isLessIce
                    itemCount = cartItem.count
                    selectedTopping.clear()
                    selectedTopping.addAll(cartItem.topping)
                    drinkViewModel.updateSelectedTopping(selectedTopping)
                    binding.edNote.setText(cartItem.note)
                    drinkViewModel.updateItemCount(itemCount)
                    setupItemCount()
                    setupView()
                    setupLayoutDrink()
                    setupLayoutRate()
                    setupLayoutCustom()
                    setupLayoutPrice()
                    setupBtnAddToCart()
                    setupLayoutTopping()
                }
            }
        }
        drinkViewModel.selectedTopping.observe(this) {
            drinkViewModel.calculateTotalPrice()
        }
        drinkViewModel.itemCount.observe(this) {
            drinkViewModel.calculateTotalPrice()
        }

    }


}