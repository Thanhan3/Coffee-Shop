
package com.example.coffeeshop.data.local

import androidx.room.TypeConverter
import com.example.coffeeshop.data.model.Topping
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ToppingConverter {

    @TypeConverter
    fun fromToppingList(topping: List<Topping>): String {
        // Tạo bản sao của danh sách trước khi chuyển đổi thành chuỗi JSON
        val toppingCopy = ArrayList(topping)
        return Gson().toJson(toppingCopy)
    }

    @TypeConverter
    fun toToppingList(toppingString: String): List<Topping> {
        val listType = object : TypeToken<List<Topping>>() {}.type
        return Gson().fromJson(toppingString, listType)
    }
}