package com.example.coffeeshop.ui.user.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.R
import com.example.coffeeshop.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupLayoutContact()

    }

    private fun setupLayoutContact() {
        binding.layoutFacebook.setOnClickListener {
            openFacebook()
        }
        binding.layoutZalo.setOnClickListener {
            openZalo()
        }
        binding.layoutPhone.setOnClickListener {
            callPhone()
        }
        binding.toolbarContact.setNavigationOnClickListener {
            finish()
        }
    }


    private fun openFacebook() {
        val facebookPage = "https://www.facebook.com/nguyen.thanh.an.513437"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookPage))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Không thể mở Facebook", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openZalo() {
        val zaloUri = "https://zalo.me/0904183684"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(zaloUri))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Không thể mở Zalo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun callPhone() {
        val phoneNumber = "tel:0904183684"
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber))
        startActivity(intent)
    }

    fun callPhone(view: View) {}

}