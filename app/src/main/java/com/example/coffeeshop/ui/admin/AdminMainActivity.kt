package com.example.coffeeshop.ui.admin

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupWithNavController
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.coffeeshop.R
import com.example.coffeeshop.databinding.ActivityAdminMainBinding


class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_admin_main)
            ?.findNavController()
        setupBottomNavigation(navController!!)
    }


    private fun setupBottomNavigation(navController: NavController) {
        val navView: BottomNavigationView = binding.navView
        navView.setupWithNavController(navController)
    }
}