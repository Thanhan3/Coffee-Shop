package com.example.coffeeshop.ui.admin.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coffeeshop.databinding.FragmentOrderBinding
import com.example.coffeeshop.ui.user.history.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        val tabLayout= binding.tabLayout
        val viewPager= binding.viewPager
        viewPager.adapter = ViewPagerAdapter(requireActivity())
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Đang xử lý"
                1 -> "Hoàn thành"
                2 -> "Đã hủy"
                else -> null
            }
        }.attach()
    }




}