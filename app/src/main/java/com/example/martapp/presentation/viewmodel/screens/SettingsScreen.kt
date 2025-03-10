package com.example.martapp.presentation.viewmodel.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.martapp.navigation.NavigationDestination

object SettingsScreenNavigation : NavigationDestination {
    override val route: String = "categories"
    override val title: String = "Categories"
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Setting Screen",
        modifier = modifier
    )
}