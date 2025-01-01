package com.example.coffeeshop.ui.admin.drink

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.coffeeshop.data.model.Drink
import com.example.coffeeshop.data.remote.RemoteDrinkDataSource
import com.example.coffeeshop.data.repository.drink.DrinkRepositoryImpl
import com.example.coffeeshop.databinding.FragmentDrinkBinding

class DrinkFragment : Fragment() {

    private lateinit var binding: FragmentDrinkBinding
    private val remoteDrinkDataSource = RemoteDrinkDataSource()
    private val drinkRepositoryImpl = DrinkRepositoryImpl(remoteDrinkDataSource)
    private val drinkViewModel: DrinkViewModel by activityViewModels {
        DrinkViewModel.Factory(drinkRepositoryImpl)
    }
    private lateinit var drinkAdapter: DrinkAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrinkBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBtnAdd()
        setupViewModel()
        setupView()
    }

    private fun setupView() {
        drinkAdapter = DrinkAdapter(
            object : onDrinkDeleteClickListener {
                override fun onDeleteClick(drink: Drink) {
                    drinkViewModel.deleteDrink(drink){
                        if (it){
                            Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT)
                                .show()
                        }else{
                            Toast.makeText(requireContext(), "Xóa thất bại", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            },object : onDrinkEditClickListener {
                override fun onEditClick(drink: Drink) {
                    val intent = Intent(requireContext(), EditDrinkActivity::class.java)
                    intent.putExtra("drinkId", drink.id)
                    intent.putExtra("categoryId", drink.category)
                    startActivity(intent)
                }
            }
        )

        binding.rvDink.adapter = drinkAdapter
    }

    private fun setupViewModel() {
        drinkViewModel.drinks.observe(viewLifecycleOwner) { drinks ->
            drinkAdapter.updateData(drinks)
        }
    }


    private fun setupBtnAdd() {
        binding.fabAddDink.setOnClickListener {
            val intent = Intent(requireContext(), AddDrinkActivity::class.java)
            startActivity(intent)
        }
    }

}