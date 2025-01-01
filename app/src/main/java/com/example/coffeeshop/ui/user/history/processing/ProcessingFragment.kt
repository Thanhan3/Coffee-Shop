package com.example.coffeeshop.ui.user.history.processing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.remote.RemoteOrderDataSource
import com.example.coffeeshop.data.repository.order.OrderRepositoryImpl
import com.example.coffeeshop.databinding.FragmentProcessingBinding
import com.example.coffeeshop.ui.user.history.follow_oder.FollowOrderActivity
import com.example.coffeeshop.utils.Constant
import com.example.coffeeshop.utils.Utils


class ProcessingFragment : Fragment() {

    private lateinit var binding: FragmentProcessingBinding
    private val remoteOrderDataSource = RemoteOrderDataSource()
    private val orderRepository = OrderRepositoryImpl(remoteOrderDataSource)
    private lateinit var adapter: ProcessingAdapter
    private lateinit var viewModel: ProcessingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProcessingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserRole()
        setupRecyclerView()
    }

    private fun setupUserRole() {
        Utils.getRoleUser { role ->
            setupViewModel(role)
        }
    }

    private fun setupFollowOrder(order: Order) {
        val intent = Intent(requireContext(), FollowOrderActivity::class.java)
        intent.putExtra("orderId", order.orderId)
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        adapter = ProcessingAdapter(object : OnClickFollowOrder {
            override fun onClickFollowOrder(order: Order) {
                setupFollowOrder(order)
            }

        })

        binding.rvProcessing.adapter = adapter
    }

    private fun setupViewModel(role: Int) {
        viewModel = ViewModelProvider(
            this,
            ProcessingViewModel.Factory(orderRepository)
        )[ProcessingViewModel::class.java]
        if(role == Constant.ADMIN){
            viewModel.allOrders.observe(viewLifecycleOwner) { orders ->
                Log.d("ProcessingFragment", "setupViewModel: $orders")
                adapter.setData(orders)
            }
        }else{
            viewModel.orders.observe(viewLifecycleOwner) { orders ->
                adapter.setData(orders)
            }
        }
    }


}