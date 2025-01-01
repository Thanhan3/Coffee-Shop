package com.example.coffeeshop.ui.user.history.cancel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.remote.RemoteOrderDataSource
import com.example.coffeeshop.data.repository.order.OrderRepositoryImpl
import com.example.coffeeshop.databinding.FragmentCancelOrderBinding
import com.example.coffeeshop.ui.user.history.complete.CompleteAdapter
import com.example.coffeeshop.ui.user.history.complete.DetailCompleteActivity
import com.example.coffeeshop.ui.user.history.complete.OnClickDetailOrder
import com.example.coffeeshop.utils.Constant
import com.example.coffeeshop.utils.Utils


class CancelOrderFragment : Fragment() {
    private lateinit var adapter: CompleteAdapter
    private val remoteOrderDataSource = RemoteOrderDataSource()
    private val orderRepository = OrderRepositoryImpl(remoteOrderDataSource)
    private val viewModel: CancelViewModel by viewModels {
        CancelViewModel.Factory(orderRepository)
    }
    private lateinit var binding: FragmentCancelOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCancelOrderBinding.inflate(layoutInflater)
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
        binding.rvCancel.adapter = adapter
    }

    private fun setupUserRole() {
        Utils.getRoleUser { role ->
            setupViewModel(role)
        }
    }

    private fun setupViewModel(role: Int) {
        if (role == Constant.ADMIN) {
            viewModel.getAllCancelOrders()
            viewModel.allOrders.observe(viewLifecycleOwner) { orders ->
                adapter.setData(orders)
            }
        } else {
            viewModel.getCancelOrders()
            viewModel.orders.observe(viewLifecycleOwner) { orders ->
                adapter.setData(orders)
            }
        }
    }
}