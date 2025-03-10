package com.example.martapp.data.repository.model

import com.example.martapp.data.repository.database.CartDao
import com.example.martapp.data.repository.database.CartItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(private val cartDao: CartDao) {

    suspend fun addToCart(cartItem: CartItemEntity) {
        cartDao.insertCartItem(cartItem)
    }

    fun getCartItems(): Flow<List<CartItemEntity>> = cartDao.getCartItems()

    suspend fun removeCartItem(productId: Int) {
        cartDao.removeCartItem(productId)
    }

    suspend fun updateCartItem(productId: Int, quantity: Int, totalPrice: Double) {
        cartDao.updateCartItem(productId, quantity, totalPrice)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}
