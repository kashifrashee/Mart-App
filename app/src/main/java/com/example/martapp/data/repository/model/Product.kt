package com.example.martapp.data.repository.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val category: String = "All",
    val stock: Int,
    val rating: Double,
    val description: String,
    @SerializedName("thumbnail") val image: String  // Use `thumbnail` as the main image
)
