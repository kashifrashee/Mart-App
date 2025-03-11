package com.example.martapp.data.repository.database.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val title: String,
    val image: String,
    val price: Double,
    val quantity: Int,
    val stock: Int = 0,
    val rating: Double = 0.0,
    val totalPrice: Double,
    val isFavorite: Boolean = false
)
