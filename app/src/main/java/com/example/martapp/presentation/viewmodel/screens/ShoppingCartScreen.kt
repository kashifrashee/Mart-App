package com.example.martapp.presentation.viewmodel.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.martapp.R
import com.example.martapp.data.repository.model.Product
import com.example.martapp.navigation.NavigationDestination
import com.example.martapp.presentation.viewmodel.ProductViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val totalPrice = cartItems.value.sumOf { it.totalPrice }

    val showReceipt = remember { mutableStateOf(false) }
    val isProcessing = remember { mutableStateOf(false) }
    val showSuccess = remember { mutableStateOf(false) }

    val selectedItems = remember { mutableStateListOf<Int>() }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Shopping Cart",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            if (cartItems.value.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Your cart is empty", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cartItems.value) { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Checkbox(
                                checked = selectedItems.contains(item.productId),
                                onCheckedChange = { checked ->
                                    if (checked) selectedItems.add(item.productId)
                                    else selectedItems.remove(item.productId)
                                }
                            )

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

                Spacer(modifier = Modifier.height(16.dp))

                // Remove Selected Items Button
                if (selectedItems.isNotEmpty()) {
                    Button(
                        onClick = {
                            selectedItems.forEach { productId ->
                                productViewModel.removeCartItem(productId)
                            }
                            selectedItems.clear()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(text = "Remove Selected Items")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Checkout Button
                Button(
                    onClick = { showReceipt.value = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Checkout")
                }
            }
        }

        if (showSuccess.value) {
            PaymentSuccessScreen {
                showSuccess.value = false
                productViewModel.clearCart()
            }
        }

        // Checkout Receipt Dialog
        if (showReceipt.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Receipt",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // List Items Neatly
                        cartItems.value.forEach { item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                            ) {
                                Text(
                                    text = "${item.title} (x${item.quantity})",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "$${"%.2f".format(item.totalPrice)}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Divider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color = Color.Gray,
                                    thickness = 2.dp
                                )
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Total Price
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$${"%.2f".format(totalPrice)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Number of Items:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${cartItems.value.size}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Checkout Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showReceipt.value = false },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text(text = "Cancel")
                            }
                            Button(
                                onClick = {
                                    isProcessing.value = true
                                    CoroutineScope(Dispatchers.Main).launch {
                                        delay(2000) // Fake 2-second processing delay
                                        isProcessing.value = false
                                        showReceipt.value = false
                                        showSuccess.value = true
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                            ) {
                                Text(text = if (isProcessing.value) "Processing..." else "Proceed")
                            }
                        }
                    }
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
    Card(
        modifier = Modifier
            .fillMaxWidth()  // Use full width for better appearance
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            Image(
                painter = rememberAsyncImagePainter(product.image),
                contentDescription = product.title,
                modifier = Modifier
                    .size(80.dp) // Increase size for better visibility
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1, // Prevent text breaking into multiple lines
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Total: $${"%.2f".format(product.price * quantity)}",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Quantity Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onDecrease,
                        enabled = quantity > 1,
                    ) {
                        Icon(painter = painterResource(R.drawable.baseline_remove_24), contentDescription = "Decrease")
                    }

                    Text(
                        text = "$quantity",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )

                    IconButton(
                        onClick = onIncrease
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }


        }
    }
}


@Composable
fun PaymentSuccessScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val composition = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.payment_success)
    )
    val progress = animateLottieCompositionAsState(
        composition = composition.value,
        iterations = 1
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition.value,
                progress = progress.value,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Payment Successful!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
            ) {
                Text(text = "OK")
            }
        }
    }
}