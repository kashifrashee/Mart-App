package com.example.martapp.data.repository.api

import com.example.martapp.data.repository.model.ProductResponse
import retrofit2.http.GET

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): ProductResponse

    @GET("products/category-list")
    suspend fun getCategories(): List<String>
}