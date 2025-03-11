package com.example.martapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.martapp.presentation.viewmodel.screens.AboutAppScreen
import com.example.martapp.presentation.viewmodel.screens.AboutScreenDestination
import com.example.martapp.presentation.viewmodel.screens.BottomNavigationBar
import com.example.martapp.presentation.viewmodel.screens.ChangePasswordScreen
import com.example.martapp.presentation.viewmodel.screens.ChangePasswordScreenNavigation
import com.example.martapp.presentation.viewmodel.screens.EditProfileScreen
import com.example.martapp.presentation.viewmodel.screens.EditProfileScreenDestination
import com.example.martapp.presentation.viewmodel.screens.FAQScreen
import com.example.martapp.presentation.viewmodel.screens.FaqScreenNavigation
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
import com.example.martapp.presentation.viewmodel.screens.ThemeSelectionScreen
import com.example.martapp.presentation.viewmodel.screens.ThemeSelectionScreenNavigation
import com.example.martapp.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun AppNavigator(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    userPreferences: UserPreferences
) {

    val startDestination = remember { mutableStateOf(LoginNavigationDestination.route) }

    LaunchedEffect(Unit) {
        userPreferences.userPhone.collect { phone ->
            startDestination.value = if (phone != null) {
                HomeScreenNavigation.route
            } else {
                LoginNavigationDestination.route
            }
        }
    }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            if (currentRoute !in listOf(
                    SignUpNavigation.route,
                    LoginNavigationDestination.route
                )
            ) {
                BottomNavigationBar(navController = navController)
            }
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
                SettingsScreen(navController = navController)
            }

            composable(FavoritesScreenNavigation.route) {
                FavoritesScreen(
                    navController = navController
                )
            }

            composable(ShoppingCartScreenNavigation.route) {
                ShoppingCartScreen(
                    navController = navController
                )
            }

            composable(EditProfileScreenDestination.route) {
                val userIdState = userPreferences.userId.collectAsState(initial = null)

                userIdState.value?.let { userId ->
                    EditProfileScreen(userId = userId, navController = navController)
                } ?: CircularProgressIndicator() // Show loader until userId is fetched
            }

            composable(ThemeSelectionScreenNavigation.route) {
                ThemeSelectionScreen()
            }

            composable(ChangePasswordScreenNavigation.route) {
                val userIdState = userPreferences.userId.collectAsState(initial = null)

                userIdState.value?.let { userId ->
                    ChangePasswordScreen(userId = userId)
                } ?: CircularProgressIndicator() // Show loader until userId is fetched
            }

            composable(FaqScreenNavigation.route) {
                FAQScreen()
            }

            composable(AboutScreenDestination.route) {
                AboutAppScreen(navController = navController)
            }
        }

    }
}

