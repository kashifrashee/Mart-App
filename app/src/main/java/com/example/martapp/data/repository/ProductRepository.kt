package com.example.martapp.data.repository

import android.util.Log
import com.example.martapp.data.repository.api.ProductApiService
import com.example.martapp.data.repository.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productApiService: ProductApiService
) {

    suspend fun fetchProducts(): List<Product> {
        return try {
            val response = productApiService.getProducts()
            Log.d("ProductRepository", "Fetched products: $response")
            response.products
        } catch (e: Exception) {
            Log.e("ProductRepository", "Failed to fetch products", e)
            emptyList()
        }
    }

    suspend fun fetchCategories(): List<String> {
        return try {
            val response = productApiService.getCategories()
            Log.d("ProductRepository", "Fetched categories: $response")
            response.map { it.replaceFirstChar { char -> char.uppercaseChar() } }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Failed to fetch categories", e)
            emptyList()
        }
    }
}