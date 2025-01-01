package com.example.coffeeshop.ui.admin.setting.topping

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.Topping
import com.example.coffeeshop.data.remote.RemoteToppingDataSource
import com.example.coffeeshop.data.repository.topping.ToppingRepositoryImpl
import com.example.coffeeshop.databinding.ActivityAddToppingBinding

class AddToppingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddToppingBinding
    private val remoteToppingDataSource = RemoteToppingDataSource()
    private val toppingRepositoryImpl = ToppingRepositoryImpl(remoteToppingDataSource)
    private lateinit var toppingViewModel: ToppingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddToppingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewModel()
        setupAddTopping()
        setupBack()
    }

    private fun setupBack() {
        binding.toolbarAddTopping.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupAddTopping() {
      binding.btnAddNew.setOnClickListener {
          if(binding.edtTopping.text.toString().isEmpty()){
              binding.edtTopping.error = "Topping không được để trống"
          }else if(binding.edtPrice.text.toString().isEmpty()){
              binding.edtPrice.error = "Giá không được để trống"
          }else{
              val toppingName = binding.edtTopping.text.toString()
              val toppingPrice = binding.edtPrice.text.toString().toInt()
              val topping = Topping(
                  name = toppingName,
                  price = toppingPrice
              )
              toppingViewModel.addTopping(topping){isSuccess->
                  if(isSuccess){
                      Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                      finish()
                  }else{
                      Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show()
                  }
              }
          }
      }
    }

    private fun setupViewModel() {
        toppingViewModel = ViewModelProvider(
            this,
            ToppingViewModel.Factory(toppingRepositoryImpl)
        )[ToppingViewModel::class.java]
    }
}