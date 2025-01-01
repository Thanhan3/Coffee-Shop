package com.example.coffeeshop.ui.user.history.complete

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.remote.RemoteOrderDataSource
import com.example.coffeeshop.data.repository.order.OrderRepositoryImpl
import com.example.coffeeshop.databinding.FragmentCompleteBinding
import com.example.coffeeshop.utils.Constant
import com.example.coffeeshop.utils.Utils


class CompleteFragment : Fragment() {
    private lateinit var adapter: CompleteAdapter
    private lateinit var binding: FragmentCompleteBinding
    private lateinit var viewModel: CompleteViewModel
    private val remoteOrderDataSource = RemoteOrderDataSource()
    private val orderRepository = OrderRepositoryImpl(remoteOrderDataSource)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompleteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserRole()
        setupView()
    }

    private fun setupView() {
        adapter = CompleteAdapter(object : OnClickDetailOrder {
            override fun onClickDetailOrder(order: Order) {
                val intent = Intent(requireContext(), DetailCompleteActivity::class.java)
                intent.putExtra("orderId", order.orderId)
                startActivity(intent)
            }
        })
        binding.rvComplete.adapter = adapter
    }


    private fun setupUserRole() {
        Utils.getRoleUser { role ->
            setupViewModel(role)
        }
    }

    private fun setupViewModel(role: Int) {
        viewModel = ViewModelProvider(
            this,
            CompleteViewModel.Factory(orderRepository)
        )[CompleteViewModel::class.java]
        if (role == Constant.ADMIN) {
            viewModel.allOrders.observe(viewLifecycleOwner) { orders ->
                adapter.setData(orders)
            }
        } else {
            viewModel.orders.observe(viewLifecycleOwner) { orders ->
                adapter.setData(orders)
            }
        }
    }


}