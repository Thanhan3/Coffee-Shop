package com.example.coffeeshop.ui.user.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coffeeshop.data.model.Category
import com.example.coffeeshop.data.remote.RemoteCategoryDataSource
import com.example.coffeeshop.data.remote.RemoteDrinkDataSource
import com.example.coffeeshop.data.repository.category.CategoryRepositoryImpl
import com.example.coffeeshop.data.repository.drink.DrinkRepositoryImpl
import com.example.coffeeshop.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private val remoteCategoryDataSource = RemoteCategoryDataSource()
    private val categoryRepositoryImpl = CategoryRepositoryImpl(remoteCategoryDataSource)
    private val remoteDrinkDataSource = RemoteDrinkDataSource()
    private val drinkRepositoryImpl = DrinkRepositoryImpl(remoteDrinkDataSource)
    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModel.Factory(drinkRepositoryImpl, categoryRepositoryImpl)
    }
    private lateinit var titleCategoryAdapter: TitleCategoryAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var drinkAdapter: DrinkAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBanner()
        setupTitleCategory()
        startAutoSlide()
        setupRvDrink()
    }

    private fun setupRvDrink() {
        drinkAdapter = DrinkAdapter()
        binding.rvDink.adapter = drinkAdapter
        homeViewModel.drinkByCategory.observe(viewLifecycleOwner) {
            drinkAdapter.updateData(it)
        }
    }


    private fun setupTitleCategory() {
        titleCategoryAdapter = TitleCategoryAdapter(object : onCategoryClickListener {
            override fun onCategoryClick(category: Category) {
                homeViewModel.getDrinkByCategory(category.id)
            }
        })
        binding.rvTitleCategory.adapter = titleCategoryAdapter
        homeViewModel.categories.observe(viewLifecycleOwner) {
            titleCategoryAdapter.updateData(it)
        }

    }

    private fun startAutoSlide() {
        val runnable = object : Runnable {
            override fun run() {
                if (currentPage == bannerAdapter.itemCount) {
                    currentPage = 0
                }
                binding.viewPagerBanner.setCurrentItem(currentPage++, true)
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable,3000)
    }

    private fun setupBanner() {
        bannerAdapter = BannerAdapter()
        binding.viewPagerBanner.adapter = bannerAdapter
        homeViewModel.outstandingDrinks.observe(viewLifecycleOwner) {
            bannerAdapter.updateData(it)
        }
        binding.wormDotsIndicator.attachTo(binding.viewPagerBanner)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }



}