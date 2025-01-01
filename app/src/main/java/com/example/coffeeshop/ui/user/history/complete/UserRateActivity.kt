package com.example.coffeeshop.ui.user.history.complete

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.Rate
import com.example.coffeeshop.databinding.ActivityUserRateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserRateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserRateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val drinkId = intent.getStringExtra("drinkId")
        val orderId = intent.getStringExtra("orderId")
        getRate(drinkId, orderId) {
            setupLayoutRate(it)
        }
        setupBtnRate(drinkId,orderId)
    }

    private fun getRate(drinkId: String?, orderId: String?, callback: (Rate?) -> Unit) {
        val ref = FirebaseDatabase.getInstance().reference.child("Rates").child(drinkId!!)
        ref.addValueEventListener(object : ValueEventListener {
            var rate: Rate? = null
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val rate = ds.getValue(Rate::class.java)
                    if (rate != null) {
                        if (rate.idRate == FirebaseAuth.getInstance().currentUser!!.uid
                            && rate.idOrder == orderId) {
                            this.rate = rate
                        }
                    }
                }
                callback(this.rate)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }

        })
    }

    private fun setupBtnRate(drinkId: String?, orderId: String?) {
        binding.btnSendRate.setOnClickListener {
            val rate = Rate(
                idRate = FirebaseAuth.getInstance().currentUser!!.uid,
                rate = binding.ratingBar.rating.toInt(),
                comment = binding.edComment.text.toString(),
                idDrink = drinkId!!,
                idOrder = orderId!!,
                idUser = FirebaseAuth.getInstance().currentUser!!.uid
            )
            val ref = FirebaseDatabase.getInstance().reference.child("Rates").child(drinkId)
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
            ref.setValue(rate).addOnSuccessListener {
                Toast.makeText(this, "Đánh giá thành công", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Đánh giá thất bại", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun setupLayoutRate(rate: Rate?) {
        rate?.rate.let {
            when (it) {
                0 -> {
                    binding.ratingBar.rating = 0F
                }

                1 -> {
                    binding.ratingBar.rating = 1F
                }

                2 -> {
                    binding.ratingBar.rating = 2F
                }

                3 -> {
                    binding.ratingBar.rating = 3F
                }

                4 -> {
                    binding.ratingBar.rating = 4F
                }

                5 -> {
                    binding.ratingBar.rating = 5F
                }

            }

        }
        binding.edComment.setText(rate?.comment)
    }
}