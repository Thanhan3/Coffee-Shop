package com.example.coffeeshop.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.R
import com.example.coffeeshop.utils.Constant
import com.example.coffeeshop.utils.Utils
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        android.os.Handler().postDelayed(Runnable {
            if (FirebaseAuth.getInstance().currentUser != null) {
                setupRole()
                return@Runnable
            } else {
                val intent = Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }
        }, 3000)
    }


    private fun setupRole() {
        Utils.getRoleUser {
            if (it == Constant.ADMIN) {
                val intent = Intent(this, com.example.coffeeshop.ui.admin.AdminMainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, com.example.coffeeshop.ui.user.UserMainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}