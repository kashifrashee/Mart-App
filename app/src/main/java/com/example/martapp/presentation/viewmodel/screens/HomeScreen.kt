package com.example.martapp.presentation.viewmodel.screens

import android.R.attr.rating
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.martapp.data.repository.database.favorite.FavoriteProductEntity
import com.example.martapp.data.repository.model.Product
import com.example.martapp.navigation.NavigationDestination
import com.example.martapp.presentation.viewmodel.FavoritesViewModel
import com.example.martapp.presentation.viewmodel.ProductViewModel

object HomeScreenNavigation : NavigationDestination {
    override val route = "home"
    override val title = "Home"
}


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavController
) {
    val products = productViewModel.products.collectAsState()
    val categories = productViewModel.categories.value
    val searchQuery = productViewModel.searchQuery.collectAsState()
    val selectedCategory = productViewModel.selectedCategory.collectAsState()
    val filteredProducts = productViewModel.filteredProducts.collectAsState()
    val isLoading = productViewModel.isLoading.value

    LaunchedEffect(Unit) {
        productViewModel.fetchProducts()
        productViewModel.fetchCategories()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        SearchBar(
            query = searchQuery.value,
            queryChange = { productViewModel.onSearchQueryChanged(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Categories Section
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (categories.isEmpty() && products.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No products or categories available", color = Color.Gray)
            }
        } else {
            LazyRow(modifier = Modifier.padding(horizontal = 8.dp)) {
                items(categories) { category ->
                    CategoryItem(
                        category.name,
                        isSelected = category.name == selectedCategory.value,
                        onClick = { productViewModel.onCategorySelected(category.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Product List Section
            Text(
                text = "Products",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredProducts.value) { product ->
                    ProductItem(
                        product = product,
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

@Composable
fun CategoryItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray)
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            text = name,
            color = if (isSelected) Color.White else Color.Black,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun ProductItem(
    product: Product,
    favoritesViewModel: FavoritesViewModel,
    onClick: () -> Unit
) {
    val isFavourite by favoritesViewModel.isFavorite(product.id).collectAsState(initial = false)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(product.image),
                contentDescription = product.title,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Stock: ${product.stock}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Rating: ${product.rating}★",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFFC107) // Golden star color
                )
            }

            IconButton(
                onClick = {
                    val favoriteEntity = FavoriteProductEntity(
                        id = product.id,
                        title = product.title,
                        image = product.image,
                        price = product.price
                    )
                    favoritesViewModel.toggleFavorite(favoriteEntity)
                }
            ) {
                Icon(
                    imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavourite) Color.Red else Color.Gray
                )
            }
        }
    }
}






@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

}