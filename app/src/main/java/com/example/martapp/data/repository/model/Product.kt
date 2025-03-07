package com.example.martapp.data.repository.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val stock: Int,
    @SerializedName("thumbnail") val image: String  // Use `thumbnail` as the main image


)
