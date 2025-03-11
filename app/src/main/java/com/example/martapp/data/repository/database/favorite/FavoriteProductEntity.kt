package com.example.martapp.data.repository.database.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String,
    val price: Double
)
