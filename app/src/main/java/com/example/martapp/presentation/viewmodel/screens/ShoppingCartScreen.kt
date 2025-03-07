package com.example.martapp.presentation.viewmodel.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.martapp.navigation.NavigationDestination

object ShoppingCartScreenNavigation : NavigationDestination {
    override val route: String = "cart"
    override val title: String = "Cart"
}

@Composable
fun ShoppingCartScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Shopping Cart Screen",
        modifier = modifier
    )
}