package com.example.coffeeshop.ui.user.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coffeeshop.authentication.LoginActivity
import com.example.coffeeshop.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AccountFragment : Fragment() {

    private lateinit var binding : FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayoutLogout()
        setupLayoutAccount()
        setupLayoutChangePassword()
        setupLayoutContact()
    }

    private fun setupLayoutContact() {
        binding.layoutContact.setOnClickListener{
            val intent = Intent(requireContext(), ContactActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLayoutAccount() {
        val ref = FirebaseDatabase.getInstance()
            .getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.get().addOnSuccessListener {
            binding.textEmail.text = it.child("email").value.toString()
        }
    }

    private fun setupLayoutChangePassword() {
        binding.layoutChangePassword.setOnClickListener{
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLayoutLogout() {
        binding.layoutLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}