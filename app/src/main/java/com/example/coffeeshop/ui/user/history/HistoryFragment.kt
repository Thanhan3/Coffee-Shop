package com.example.coffeeshop.ui.user.history


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coffeeshop.databinding.FragmentHistoryBinding
import com.google.android.material.tabs.TabLayoutMediator

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupViewPager()
    }

    private fun setupViewPager() {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager
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