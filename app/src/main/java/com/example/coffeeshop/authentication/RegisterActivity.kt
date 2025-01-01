package com.example.coffeeshop.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.User
import com.example.coffeeshop.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.database.values

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupLayoutRole()
        setupLogin()
        setupBtnRegister()


    }

    private fun setupBtnRegister() {

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkRegisterButtonState()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.edtEmail.addTextChangedListener(textWatcher)
        binding.edtPassword.addTextChangedListener(textWatcher)
        binding.edtKeyAdmin.addTextChangedListener(textWatcher)

        binding.rgRole.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbAdmin.id -> {
                    checkRegisterButtonState()
                }

                binding.rbUser.id -> {
                    checkRegisterButtonState()
                }
            }
        }
        if (binding.rbAdmin.isChecked) {
            binding.btnRegister.setOnClickListener {

            }
        }
        binding.btnRegister.setOnClickListener {
            if (binding.rbUser.isChecked) {
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPassword.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val user = User(
                                    FirebaseAuth.getInstance().currentUser!!.uid,
                                    email,
                                    "1")
                                Firebase.database.getReference("Users")
                                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .setValue(user)
                                    .addOnCompleteListener { saveTask ->
                                        if (saveTask.isSuccessful) {
                                            // Hiển thị thông báo thành công
                                            Toast.makeText(
                                                this,
                                                "Đăng ký thành công!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            // Thông báo lỗi lưu dữ liệu
                                            Toast.makeText(
                                                this,
                                                "Không thể lưu dữ liệu!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                // Hiển thị thông báo lỗi đăng ký
                                Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Thông báo yêu cầu nhập đủ thông tin
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT)
                        .show()
                }
            } else if (binding.rbAdmin.isChecked) {
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPassword.text.toString().trim()
                val keyAdmin = binding.edtKeyAdmin.text.toString().trim()
                if (email.isNotEmpty() && password.isNotEmpty() && keyAdmin.isNotEmpty()) {
                    val reference = Firebase.database.getReference("Admin/key")
                    reference.get().addOnSuccessListener { dataSnapshot ->
                        val keyAdminSaved = dataSnapshot.value.toString()
                        if (keyAdmin != keyAdminSaved) {
                            Toast.makeText(
                                this, "Key không đúng , vui lòng thử lại", Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        val user = User(
                                            FirebaseAuth.getInstance().currentUser!!.uid,
                                            email,
                                            "0")
                                        Firebase.database.getReference("Users")
                                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .setValue(user)
                                            .addOnCompleteListener { saveTask ->
                                                if (saveTask.isSuccessful) {
                                                    Toast.makeText(
                                                        this,
                                                        "Đăng ký thành công!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        this,
                                                        "Không thể lưu dữ liệu!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Đăng ký thất bại!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(this, "có lỗi xảy ra , vui lòng thử lại", Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun checkRegisterButtonState() {
        val email: String = binding.edtEmail.getText().toString().trim()
        val password: String = binding.edtPassword.getText().toString().trim()
        val keyAdmin: String = binding.edtKeyAdmin.getText().toString().trim()
        if (binding.rbAdmin.isChecked) {
            binding.btnRegister.setEnabled(email.isNotEmpty() && password.isNotEmpty() && keyAdmin.isNotEmpty())
        } else {
            binding.btnRegister.setEnabled(email.isNotEmpty() && password.isNotEmpty())
        }
    }

    private fun setupLayoutRole() {
        binding.rbUser.isChecked = true
        binding.rbAdmin.setOnClickListener {
            binding.layoutKeyAdmin.visibility = View.VISIBLE
        }
        binding.rbUser.setOnClickListener {
            binding.layoutKeyAdmin.visibility = View.GONE
        }
    }


    private fun setupLogin() {
        binding.textLogin.setOnClickListener {
            onBackPressed()
        }
    }
}