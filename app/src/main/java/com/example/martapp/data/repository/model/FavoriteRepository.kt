package com.example.martapp.data.repository.model

import com.example.martapp.data.repository.database.favorite.FavoriteDao
import com.example.martapp.data.repository.database.favorite.FavoriteProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {
    fun getAllFavorites(): Flow<List<FavoriteProductEntity>> = favoriteDao.getAllFavorites()

    fun isFavorite(productId: Int): Flow<Boolean> = favoriteDao.isFavorite(productId)

    suspend fun toggleFavorite(product: FavoriteProductEntity) {
        if (favoriteDao.isFavorite(product.id).first()) {
            favoriteDao.deleteFavorite(product)
        } else {
            favoriteDao.insertFavorite(product)
        }
    }
}