package com.example.martapp.presentation.viewmodel.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.martapp.R
import com.example.martapp.data.repository.database.favorite.FavoriteProductEntity
import com.example.martapp.navigation.NavigationDestination
import com.example.martapp.presentation.viewmodel.FavoritesViewModel
import com.example.martapp.presentation.viewmodel.ProductViewModel

object ProductDetailsNavigation : NavigationDestination {
    override val route = "productDetails"
    override val title = "Product Details"
}

@SuppressLint("DefaultLocale")
@Composable
fun ProductDetailsScreen(
    productId: Int,
    productViewModel: ProductViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavController
) {
    val product = productViewModel.selectedProduct.collectAsState()
    val quantity = remember { mutableIntStateOf(1) }

    val favoriteProducts by favoritesViewModel.favoriteProducts.collectAsState()
    val isFavorite = favoriteProducts.any { it.id == productId }

    val context = LocalContext.current
    val cartAddSuccess = productViewModel.cartAddSuccess.collectAsState(initial = null)

    LaunchedEffect(productId) {
        Log.d("ProductDetailsScreen", "Fetching product with id: $productId")
        productViewModel.fetchProductById(productId)
    }

    LaunchedEffect(cartAddSuccess.value) {
        cartAddSuccess.value?.let { success ->
            Toast.makeText(
                context,
                if (success) "Added to cart successfully!" else "Failed to add to cart!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    when {
        product.value == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        else -> {
            val productData = product.value!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.elevatedCardElevation(6.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(productData.image),
                        contentDescription = productData.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = productData.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$${productData.price}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Stock: ${productData.stock}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (productData.stock > 0) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Rating: ${productData.rating}★",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = productData.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(
                        onClick = { if (quantity.intValue > 1) quantity.intValue-- },
                        enabled = quantity.intValue > 1
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_remove_24),
                            contentDescription = "Decrease",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }

                    Text(
                        text = quantity.intValue.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { if (quantity.intValue < productData.stock) quantity.intValue++ },
                        enabled = quantity.intValue < productData.stock
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_add_24),
                            contentDescription = "Increase",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Total: $${
                        String.format("%.2f", productData.price * quantity.intValue)
                    }",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { productViewModel.addToCart(productData, quantity.intValue) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = productData.stock > 0
                ) {
                    Text(text = if (productData.stock > 0) "Add to Cart" else "Out of Stock")
                }

                Button(
                    onClick = {
                        val favoriteEntity = FavoriteProductEntity(
                            id = productData.id,
                            title = productData.title,
                            image = productData.image,
                            price = productData.price
                        )
                        favoritesViewModel.toggleFavorite(favoriteEntity)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


/*
productViewModel.addToCart(productData.id, quantity.intValue)
Log.d("ProductDetailsScreen", "Product added to cart: $productId, Quantity: ${quantity.intValue}")*/
