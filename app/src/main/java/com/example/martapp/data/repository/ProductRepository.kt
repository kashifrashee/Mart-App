package com.example.martapp.data.repository

import android.util.Log
import com.example.martapp.data.repository.api.ProductApiService
import com.example.martapp.data.repository.database.CartDao
import com.example.martapp.data.repository.database.CartItemEntity
import com.example.martapp.data.repository.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productApiService: ProductApiService,
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

    suspend fun fetchProductById(productId: Int): Product? {
        return try {
            val product = productApiService.getProductById(productId)
            Log.d("ProductRepository", "Fetched product: $product")
            product
        } catch (e: Exception) {
            Log.e("ProductRepository", "Failed to fetch product", e)
            null
        }
    }


}