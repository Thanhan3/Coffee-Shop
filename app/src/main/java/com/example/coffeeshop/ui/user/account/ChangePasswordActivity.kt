package com.example.coffeeshop.ui.user.account

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.R
import com.example.coffeeshop.databinding.ActivityChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupToolbar()
        setupBtnChangePassword()

    }

    private fun setupBtnChangePassword() {
        binding.btnChangePassword.setOnClickListener {
            when {
                binding.edtPasswordOld.text.toString().isEmpty() -> {
                    binding.layoutPasswordOld.error = "Vui lòng nhập mật khẩu cũ"
                }
                binding.edtPasswordNew.text.toString().isEmpty() -> {
                    binding.layoutPasswordNew.error = "Vui lòng nhập mật khẩu mới"
                }
                binding.edtPasswordNew.text.toString().length < 6 -> {
                    binding.layoutPasswordNew.error = "Mật khẩu phải có ít nhất 6 ký tự"
                }
                binding.edtConfirmPassword.text.toString() != binding.edtPasswordNew.text.toString() -> {
                    binding.layoutConfirmPassword.error = "Mật khẩu không khớp"
                }
                else -> {
                    reAuthenticateAndChangePassword()
                }
            }
        }
    }

    private fun reAuthenticateAndChangePassword() {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email ?: ""
        val oldPassword = binding.edtPasswordOld.text.toString()
        val newPassword = binding.edtPasswordNew.text.toString()

        val credential = EmailAuthProvider.getCredential(email, oldPassword)
        user?.reauthenticate(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Đổi mật khẩu thành công!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Đổi mật khẩu thất bại: ${updateTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Mật khẩu cũ không chính xác: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setupToolbar() {
        binding.toolbarChangePassword.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}