package com.example.coffeeshop

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.coffeeshop.data.local.AppDatabase

class MyApplication : Application() {
    val appDatabase: AppDatabase by lazy { AppDatabase.getInstance(this) }
}