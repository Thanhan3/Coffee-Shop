package com.example.coffeeshop.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.UUID

object Utils {

    fun uploadImage(
        uri: Uri,
        folderName: String,
        progressDialog: ProgressDialog,
        callback: (String?) -> Unit
    ) {
        progressDialog.setTitle("Uploading...")
        progressDialog.show()

        var imageUrl: String?
        FirebaseStorage.getInstance()
            .getReference(folderName)
            .child(UUID.randomUUID().toString())
            .putFile(uri)
            .addOnSuccessListener {
                progressDialog.dismiss()
                it.storage.downloadUrl.addOnSuccessListener {
                    imageUrl = it.toString()
                    callback(imageUrl)
                }
            }.addOnProgressListener { taskSnapshot ->
                // Tính phần trăm tải lên
                val progress =
                    (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
                progressDialog.setMessage("Uploaded $progress%")
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                callback(null)
            }
    }

    fun getFinalPrice(price: Int, discount: Int): Int {
        return price - (price * discount / 100)
    }

    fun getDiscountPrice(price: Int, discount: Int): Int {
        return (price * discount / 100)
    }

    fun getRoleUser(callback: (Int) -> Unit) {
        val ref = FirebaseDatabase
            .getInstance()
            .getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userRole = snapshot.child("role").value.toString().toInt()
                    callback(userRole)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    fun convertMillisToDate(millis: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val date = Date(millis)
        return dateFormat.format(date)
    }

    fun isToday(timeInMillis: Long): Boolean {
        val calendar = Calendar.getInstance()

        val today = Calendar.getInstance()
        today.timeInMillis = System.currentTimeMillis()
        val todayYear = today.get(Calendar.YEAR)
        val todayMonth = today.get(Calendar.MONTH)
        val todayDay = today.get(Calendar.DAY_OF_MONTH)

        calendar.timeInMillis = timeInMillis
        val inputYear = calendar.get(Calendar.YEAR)
        val inputMonth = calendar.get(Calendar.MONTH)
        val inputDay = calendar.get(Calendar.DAY_OF_MONTH)

        return todayYear == inputYear && todayMonth == inputMonth && todayDay == inputDay
    }
}