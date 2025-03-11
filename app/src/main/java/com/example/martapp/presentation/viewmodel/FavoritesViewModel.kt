package com.example.martapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.martapp.data.repository.database.favorite.FavoriteProductEntity
import com.example.martapp.data.repository.model.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoriteRepository
) : ViewModel(){

    private val _favoriteProducts = MutableStateFlow<List<FavoriteProductEntity>>(emptyList())
    val favoriteProducts: StateFlow<List<FavoriteProductEntity>> = _favoriteProducts.asStateFlow()

    init {
        getFavoriteProducts()
    }

    private fun getFavoriteProducts() {
        viewModelScope.launch {
            repository.getAllFavorites().collect { favorites ->
                _favoriteProducts.value = favorites
            }
        }
    }

    fun isFavorite(productId: Int): Flow<Boolean> {
        return repository.isFavorite(productId)
    }

    fun toggleFavorite(product: FavoriteProductEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(product)
        }
    }
}