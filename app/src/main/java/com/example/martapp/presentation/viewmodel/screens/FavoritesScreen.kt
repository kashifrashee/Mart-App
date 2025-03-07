package com.example.martapp.presentation.viewmodel.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.martapp.navigation.NavigationDestination

object FavoritesScreenNavigation : NavigationDestination {
    override val route: String = "favorites"
    override val title: String = "Favorites"
}

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Favorites Screen",
        modifier = modifier
    )
}