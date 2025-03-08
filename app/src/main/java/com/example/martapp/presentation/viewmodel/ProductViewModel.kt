package com.example.martapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.martapp.data.repository.ProductRepository
import com.example.martapp.data.repository.database.CartItemEntity
import com.example.martapp.data.repository.model.CartRepository
import com.example.martapp.data.repository.model.Category
import com.example.martapp.data.repository.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {


    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _categories = mutableStateOf<List<Category>>(emptyList())
    val categories = _categories

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    private val _cartItems = MutableStateFlow<List<CartItemEntity>>(emptyList())
    val cartItems: StateFlow<List<CartItemEntity>> = _cartItems

    private val _cartProducts = MutableStateFlow<List<Product>>(emptyList())
    val cartProducts: StateFlow<List<Product>> = _cartProducts.asStateFlow()

    private val _cartAddSuccess = MutableSharedFlow<Boolean>()
    val cartAddSuccess = _cartAddSuccess.asSharedFlow()


    private val _isLoading = mutableStateOf(false) // Loading state
    val isLoading = _isLoading

    init {
        fetchCartItems()
    }

    fun fetchCartItems() {
        viewModelScope.launch {
            cartRepository.getCartItems().collect { items ->
                _cartItems.value = items
            }
        }
    }

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

    fun fetchProductById(productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true  // Show loading indicator
            try {
                _selectedProduct.value = repository.fetchProductById(productId)
                Log.d("ProductViewModel", "Fetched product: ${_selectedProduct.value}")
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error fetching product", e)
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

    fun addToCart(product: Product, quantity: Int) {
        viewModelScope.launch {
            try {
                val totalPrice = product.price * quantity
                val cartItem = CartItemEntity(
                    productId = product.id,
                    title = product.title,
                    image = product.image,
                    price = product.price,
                    quantity = quantity,
                    totalPrice = totalPrice
                )
                cartRepository.addToCart(cartItem)
                Log.d("ProductViewModel", "Added to cart: $cartItem")

                _cartAddSuccess.emit(true)  // Notify success
                fetchCartItems()
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Failed to add to cart", e)
                _cartAddSuccess.emit(false)  // Notify failure
            }
        }
    }

    fun updateCartItem(productId: Int, quantity: Int, totalPrice: Double) {
        viewModelScope.launch {
            cartRepository.updateCartItem(productId, quantity, totalPrice)
            Log.d("ProductViewModel", "Updated cart item: productId=$productId, quantity=$quantity, totalPrice=$totalPrice")
            fetchCartItems()
        }
    }

    fun removeCartItem(productId: Int) {
        viewModelScope.launch {
            cartRepository.removeCartItem(productId)
            Log.d("ProductViewModel", "Removed cart item: productId=$productId")
            fetchCartItems()
        }
    }


}