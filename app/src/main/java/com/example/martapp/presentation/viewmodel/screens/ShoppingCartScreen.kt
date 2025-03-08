package com.example.martapp.presentation.viewmodel.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.martapp.data.repository.model.Product
import com.example.martapp.navigation.NavigationDestination
import com.example.martapp.presentation.viewmodel.ProductViewModel
import com.example.martapp.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

object ShoppingCartScreenNavigation : NavigationDestination {
    override val route: String = "cart"
    override val title: String = "Cart"
}

@Composable
fun ShoppingCartScreen(
    productViewModel: ProductViewModel = hiltViewModel(),
    navController: NavController
) {
    val cartItems = productViewModel.cartItems.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Shopping Cart",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        if (cartItems.value.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Your cart is empty", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn {
                items(cartItems.value) { item ->
                    CartItem(
                        product = Product(
                            id = item.productId,
                            title = item.title,
                            image = item.image,
                            price = item.price,
                            stock = 0,
                            rating = 0.0,
                            description = ""
                        ),
                        quantity = item.quantity,
                        onIncrease = {
                            productViewModel.updateCartItem(
                                item.productId,
                                item.quantity + 1,
                                item.price * (item.quantity + 1)
                            )
                        },
                        onDecrease = {
                            if (item.quantity > 1) {
                                productViewModel.updateCartItem(
                                    item.productId,
                                    item.quantity - 1,
                                    item.price * (item.quantity - 1)
                                )
                            } else {
                                productViewModel.removeCartItem(item.productId)
                            }
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun CartItem(
    product: Product,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    val totalPrice = product.price * quantity  // Calculate total price

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(product.image),
                contentDescription = product.title,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.title, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Total: $${"%.2f".format(totalPrice)}",  // Show total price
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { if (quantity > 1) onDecrease() },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Red) // Red for Decrease
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_remove_24),
                        contentDescription = "Decrease"
                    )
                }
                Text(text = quantity.toString(), modifier = Modifier.padding(8.dp))
                IconButton(
                    onClick = { onIncrease() },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Green) // Green for Add
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }
        }
    }
}
