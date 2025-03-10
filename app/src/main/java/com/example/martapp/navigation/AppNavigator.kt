package com.example.martapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.martapp.presentation.viewmodel.screens.BottomNavigationBar
import com.example.martapp.presentation.viewmodel.screens.SettingsScreen
import com.example.martapp.presentation.viewmodel.screens.SettingsScreenNavigation
import com.example.martapp.presentation.viewmodel.screens.FavoritesScreen
import com.example.martapp.presentation.viewmodel.screens.FavoritesScreenNavigation
import com.example.martapp.presentation.viewmodel.screens.HomeScreen
import com.example.martapp.presentation.viewmodel.screens.HomeScreenNavigation
import com.example.martapp.presentation.viewmodel.screens.LoginNavigationDestination
import com.example.martapp.presentation.viewmodel.screens.LoginScreen
import com.example.martapp.presentation.viewmodel.screens.ProductDetailsNavigation
import com.example.martapp.presentation.viewmodel.screens.ProductDetailsScreen
import com.example.martapp.presentation.viewmodel.screens.ShoppingCartScreen
import com.example.martapp.presentation.viewmodel.screens.ShoppingCartScreenNavigation
import com.example.martapp.presentation.viewmodel.screens.SignUpNavigation
import com.example.martapp.presentation.viewmodel.screens.SignUpScreen
import com.example.martapp.utils.UserPreferences

@Composable
fun AppNavigator(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    userPreferences: UserPreferences
) {

    var startDestination = remember { mutableStateOf(LoginNavigationDestination.route) }

    LaunchedEffect(Unit) {
        userPreferences.userPhone.collect { phone ->
            startDestination.value = if (phone != null) {
                HomeScreenNavigation.route
            } else {
                LoginNavigationDestination.route
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination.value,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(SignUpNavigation.route) {
                SignUpScreen(navController = navController)
            }

            composable(LoginNavigationDestination.route) {
                LoginScreen(
                    navController = navController
                )
            }

            composable(HomeScreenNavigation.route) {
                HomeScreen(navController = navController)
            }

            composable("${ProductDetailsNavigation.route}/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
                productId?.let {
                    ProductDetailsScreen(
                        productId = it,
                        navController = navController
                    )
                }

            }

            composable(SettingsScreenNavigation.route) {
                SettingsScreen()
            }

            composable(FavoritesScreenNavigation.route) {
                FavoritesScreen()
            }

            composable(ShoppingCartScreenNavigation.route) {
                ShoppingCartScreen(
                    navController = navController
                )
            }

        }

    }


}

