package com.example.martapp.data.repository.api

import com.example.martapp.data.repository.model.Product
import com.example.martapp.data.repository.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): ProductResponse

    @GET("products/category-list")
    suspend fun getCategories(): List<String>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Product

    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): ProductResponse

}