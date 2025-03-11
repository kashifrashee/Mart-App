package com.example.martapp.data.repository.database.cart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItemEntity: CartItemEntity)

    @Query("SELECT * FROM cart_items")
    fun getCartItems() : Flow<List<CartItemEntity>>

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun removeCartItem(productId: Int)

    @Query("UPDATE cart_items SET quantity = :quantity, totalPrice = :totalPrice WHERE productId = :productId")
    suspend fun updateCartItem(productId: Int, quantity: Int, totalPrice: Double)


    @Query("DELETE FROM cart_items")
    suspend fun clearCart()


    @Query("SELECT * FROM cart_items WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getProductById(productId: Int): CartItemEntity?

    @Query("UPDATE cart_items SET isFavorite = :isFav WHERE productId = :productId")
    suspend fun updateFavoriteStatus(productId: Int, isFav: Boolean)
}