package com.example.martapp.presentation.viewmodel.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.martapp.R
import com.example.martapp.data.repository.model.Product
import com.example.martapp.navigation.NavigationDestination
import com.example.martapp.presentation.viewmodel.FavoritesViewModel
import com.example.martapp.presentation.viewmodel.ProductViewModel

object FavoritesScreenNavigation : NavigationDestination {
    override val route: String = "favorites"
    override val title: String = "Favorites"
}

@Composable
fun FavoritesScreen(
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavController
) {
    val favoriteProducts by favoritesViewModel.favoriteProducts.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Favorite Products",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (favoriteProducts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.baseline_question_mark_24), // Add an empty state illustration
                        contentDescription = "No Favorites",
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        text = "No favorites yet!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                items(favoriteProducts, key = { it.id }) { product ->
                    AnimatedVisibility(visible = true) {
                        ProductItem(
                            product = Product(
                                id = product.id,
                                title = product.title,
                                image = product.image,
                                price = product.price,
                                description = "",
                                stock = 0,
                                rating = 0.0
                            ),
                            favoritesViewModel = favoritesViewModel,
                            onClick = {
                                navController.navigate("${ProductDetailsNavigation.route}/${product.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}
