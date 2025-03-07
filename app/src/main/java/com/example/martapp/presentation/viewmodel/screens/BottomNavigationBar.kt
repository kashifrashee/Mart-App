package com.example.martapp.presentation.viewmodel.screens

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.martapp.R

data class NavigationItem(
    val title: String,
    val icon: Int,
    val route: String
)


@Composable
fun BottomNavigationBar(
    navController: NavController,
) {
    val selectedNavigationIndex = rememberSaveable {
        mutableIntStateOf(0)
    }

    val navigationItems = listOf(
        NavigationItem(
            "Home",
            R.drawable.baseline_home_max_24,
            HomeScreenNavigation.route
        ),

        NavigationItem(
            "Categories",
            R.drawable.baseline_category_24,
            CategoriesScreenNavigation.route
        ),
        NavigationItem(
            "Favorites",
            R.drawable.baseline_favorite_border_24,
            FavoritesScreenNavigation.route
        ),
        NavigationItem(
            "Cart",
            R.drawable.baseline_add_shopping_cart_24,
            ShoppingCartScreenNavigation.route
        ),
    )

    NavigationBar(
        containerColor = Color.White,
    ) {
        navigationItems.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = selectedNavigationIndex.intValue == index,
                onClick = {
                    selectedNavigationIndex.intValue = index
                    navController.navigate(navigationItem.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(navigationItem.icon),
                        contentDescription = navigationItem.title
                    )
                },
                label = {
                    Text(
                        navigationItem.title,
                        color = if (index == selectedNavigationIndex.intValue)
                            Color.Black
                        else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.surface,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }

}