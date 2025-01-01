package com.example.coffeeshop.ui.user.cart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.local.AppDatabase
import com.example.coffeeshop.data.remote.RemoteAddressDataSource
import com.example.coffeeshop.data.remote.RemoteDiscountDataSource
import com.example.coffeeshop.data.repository.address.AddressRepositoryImpl
import com.example.coffeeshop.data.repository.discount.DiscountRepositoryImpl
import com.example.coffeeshop.databinding.FragmentMiniCartBinding

class MiniCartFragment : Fragment() {
    private lateinit var cartViewModel: CartViewModel
    private lateinit var appDatabase: AppDatabase
    private lateinit var binding: FragmentMiniCartBinding
    private val remoteDiscountDataSource = RemoteDiscountDataSource()
    private val discountRepositoryImpl = DiscountRepositoryImpl(remoteDiscountDataSource)
    private val remoteAddressDataSource = RemoteAddressDataSource()
    private val addressRepositoryImpl = AddressRepositoryImpl(remoteAddressDataSource)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMiniCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupView()
    }

    private fun setupView() {
        binding.root.setOnClickListener{
            val intent = Intent(requireContext(), DetailCartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        appDatabase = AppDatabase.getInstance(requireContext())
        val cartDao = appDatabase.cartDao()
        cartViewModel = ViewModelProvider(this, CartViewModel.Factory(
            cartDao, discountRepositoryImpl,addressRepositoryImpl
        ))[CartViewModel::class.java]
        cartViewModel.totalPrice.observe(viewLifecycleOwner){
            binding.textPrice.text = it.toString()+".000vnd"
        }
        cartViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            binding.title.text = cartItems.size.toString()+" Đồ uống"
        }
    }
}


