package com.example.martapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.martapp.data.repository.ProductRepository
import com.example.martapp.data.repository.model.Category
import com.example.martapp.data.repository.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {


    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _categories = mutableStateOf<List<Category>>(emptyList())
    val categories = _categories

    private val _isLoading = mutableStateOf(false) // Loading state
    val isLoading = _isLoading

    fun fetchProducts(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!forceRefresh && _products.value.isNotEmpty())
                return@launch

            _isLoading.value = true  // Show loading indicator
            try {
                _products.value = repository.fetchProducts()
                Log.d("ProductViewModel", "Fetched products: ${_products.value}")
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error fetching products", e)
            } finally {
                _isLoading.value = false  // Hide loading indicator
            }
        }
    }


    fun fetchCategories(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            if (!forceRefresh && categories.value.isNotEmpty())
                return@launch

            _isLoading.value = true  // Show loading indicator
            try {
                _categories.value = repository.fetchCategories().map { Category(it) }
                Log.d("ProductViewModel Category", "Fetched categories: ${categories.value}")
            } catch (e: Exception) {
                Log.e("ProductViewModel Category", "Error fetching categories", e)
            } finally {
                _isLoading.value = false  // Hide loading indicator
            }
        }
    }


}