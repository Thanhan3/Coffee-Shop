package com.example.coffeeshop.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.User
import com.example.coffeeshop.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupBtnLogin()
        setupBtnRegister()
        setupBtnLoginListener()
        setupRgRole()
    }

    private fun setupRgRole() {
        binding.rbUser.isChecked = true
    }

    private fun setupBtnLoginListener() {
        binding.btnLogin.setOnClickListener {
            if (binding.rbUser.isChecked) {
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPassword.text.toString().trim()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            checkRoleUser(FirebaseAuth.getInstance().currentUser!!.uid)
                        } else {
                            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            if(binding.rbAdmin.isChecked){
                val email = binding.edtEmail.text.toString().trim()
                val password = binding.edtPassword.text.toString().trim()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            checkRoleAdmin(FirebaseAuth.getInstance().currentUser!!.uid)
                        } else {
                            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }


    private fun setupBtnRegister() {
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBtnLogin() {
        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkLoginButtonState();
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkLoginButtonState();
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


    }

    private fun checkLoginButtonState() {
        val email: String = binding.edtEmail.getText().toString().trim()
        val password: String = binding.edtPassword.getText().toString().trim()
        binding.btnLogin.setEnabled(email.isNotEmpty() && password.isNotEmpty())

    }

    private fun checkRoleUser(userId :String){
        Firebase.database.getReference().child("Users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val user = snapshot.getValue(User::class.java)
                        if(user != null && user.role == "1"){
                            val intent = Intent(this@LoginActivity, com.example.coffeeshop.ui.user.UserMainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this@LoginActivity,"Không tìm thấy người dùng",Toast.LENGTH_SHORT).show()
                            FirebaseAuth.getInstance().signOut()
                        }
                    }else{
                        Toast.makeText(this@LoginActivity,"Không tìm thấy người dùng",Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity,"Có lỗi xảy ra!!",Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                }

            })
    }
    private fun checkRoleAdmin(userId : String) {
        Firebase.database.getReference().child("Users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val user = snapshot.getValue(User::class.java)
                        if(user != null && user.role == "0"){
                            val intent = Intent(this@LoginActivity, com.example.coffeeshop.ui.admin.AdminMainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this@LoginActivity,"Không tìm thấy admin",Toast.LENGTH_SHORT).show()
                            FirebaseAuth.getInstance().signOut()
                        }
                    }else{
                        Toast.makeText(this@LoginActivity,"Không tìm thấy người dùng",Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@LoginActivity,"Có lỗi xảy ra!!",Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                }

            })
    }
}