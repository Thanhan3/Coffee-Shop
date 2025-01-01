package com.example.coffeeshop.ui.user.cart.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.local.AppDatabase
import com.example.coffeeshop.data.model.Address
import com.example.coffeeshop.data.remote.RemoteAddressDataSource
import com.example.coffeeshop.data.remote.RemoteDiscountDataSource
import com.example.coffeeshop.data.repository.address.AddressRepositoryImpl
import com.example.coffeeshop.data.repository.discount.DiscountRepositoryImpl
import com.example.coffeeshop.databinding.FragmentAddAddressBinding
import com.example.coffeeshop.ui.user.cart.CartViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddAddressFragment : BottomSheetDialogFragment() {

    private val remoteDiscountDataSource = RemoteDiscountDataSource()
    private val discountRepositoryImpl = DiscountRepositoryImpl(remoteDiscountDataSource)
    private val remoteAddressDataSource = RemoteAddressDataSource()
    private val addressRepositoryImpl = AddressRepositoryImpl(remoteAddressDataSource)
    private lateinit var cartViewModel: CartViewModel
    private lateinit var appDatabase: AppDatabase

    private lateinit var binding: FragmentAddAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAddressBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupBtnAdd()
        setupBtnCancel()
    }

    private fun setupBtnCancel() {
        binding.btnCancel.setOnClickListener{
            dismiss()
        }
    }

    private fun setupBtnAdd() {
        binding.btnAdd.setOnClickListener {
            if(binding.edtAddress.text.toString().isEmpty()){
                binding.edtAddress.error = "Vui lòng nhập địa chỉ"
            }else if(binding.edtName.text.toString().isEmpty()){
                binding.edtName.error = "Vui lòng nhập tên"
            }else if(binding.edtNumberPhone.text.toString().isEmpty()){
                binding.edtNumberPhone.error = "Vui lòng nhập số điện thoại"
            }else{
                val address = binding.edtAddress.text.toString()
                val name = binding.edtName.text.toString()
                val numberPhone = binding.edtNumberPhone.text.toString()
                val newAddress = Address(
                    address = address,
                    receiverName = name,
                    numberPhone = numberPhone)
                cartViewModel.addAddress(newAddress){
                    if(it){
                        Toast.makeText(requireContext(), "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }else{
                        Toast.makeText(requireContext(), "Thêm địa chỉ thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }


    private fun setupViewModel() {
        appDatabase = AppDatabase.getInstance(requireContext())
        val cartDao = appDatabase.cartDao()
        cartViewModel = ViewModelProvider(
            this,
            CartViewModel.Factory(cartDao, discountRepositoryImpl,addressRepositoryImpl)
        )[CartViewModel::class.java]
    }








    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 400
        }
    }
}