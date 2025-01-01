package com.example.coffeeshop.ui.admin.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coffeeshop.authentication.LoginActivity
import com.example.coffeeshop.databinding.FragmentSettingBinding
import com.example.coffeeshop.ui.admin.setting.discount.DiscountActivity
import com.example.coffeeshop.ui.admin.setting.statistics.StatisticsActivity
import com.example.coffeeshop.ui.admin.setting.topping.ToppingActivity
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupLogout()
        setupLayoutStatistics()
    }

    private fun setupLayoutStatistics() {
        binding.layoutSalesSummary.setOnClickListener{
            val intent = Intent(requireContext(), StatisticsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLogout() {
        binding.layoutLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setupView() {
        binding.layoutTopping.setOnClickListener{
            val intent = Intent(requireContext(), ToppingActivity::class.java)
            startActivity(intent)
        }
        binding.layoutDiscount.setOnClickListener{
            val intent = Intent(requireContext(), DiscountActivity::class.java)
            startActivity(intent)
        }

    }

}