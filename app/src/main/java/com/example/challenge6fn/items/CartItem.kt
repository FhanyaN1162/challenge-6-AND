package com.example.challenge6fn.items

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodName: String,
    var totalPrice: Int,
    var price: Int,
    var quantity: Int,
    val imageResourceId: String,
    var note: String? = ""
){
    fun calculateTotalPrice(): Int {
        return quantity * price
    }
}

