package com.example.coffeeshop.ui.user.history

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.coffeeshop.ui.user.history.cancel.CancelOrderFragment
import com.example.coffeeshop.ui.user.history.complete.CompleteFragment
import com.example.coffeeshop.ui.user.history.processing.ProcessingFragment


class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProcessingFragment()
            1 -> CompleteFragment()
            2 -> CancelOrderFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}