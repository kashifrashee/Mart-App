package com.example.martapp.presentation.viewmodel.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.martapp.navigation.NavigationDestination
import com.example.martapp.presentation.viewmodel.ProductViewModel

object HomeScreenNavigation : NavigationDestination {
    override val route = "home"
    override val title = "Home"
}


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    productViewModel: ProductViewModel = hiltViewModel()
) {
    val products = productViewModel.products.collectAsState()
    val categories = productViewModel.categories.value
    val isLoading = productViewModel.isLoading.value

    LaunchedEffect(Unit) {
        productViewModel.fetchProducts()
        productViewModel.fetchCategories()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(query = "", queryChange = { })

        // Categories Section
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (categories.isEmpty() && products.value.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No products or categories available", color = Color.Gray)
            }
        } else {
            LazyRow(modifier = Modifier.padding(8.dp)) {
                items(categories) { category ->
                    CategoryItem(category.name)
                }
            }

            // Product List Section
            Text(
                text = "Products",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)
            )
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(products.value) { product ->
                    ProductItem(product.title, product.image, product.price, product.stock)
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: String) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(text = category, color = Color.Black)
        }
    }
}

@Composable
fun ProductItem(title: String, image: String, price: Double, stock: Int) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter(image),
                contentDescription = title,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                Text(text = "$${price}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(text = "$stock", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}



