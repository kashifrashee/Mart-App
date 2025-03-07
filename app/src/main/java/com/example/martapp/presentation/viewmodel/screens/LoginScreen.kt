package com.example.martapp.presentation.viewmodel.screens

import android.util.Log
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.martapp.R
import com.example.martapp.navigation.NavigationDestination
import com.example.martapp.presentation.viewmodel.AuthViewModel

object LoginNavigationDestination : NavigationDestination {
    override val route: String = "login"
    override val title: String = "Login"
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    var phoneNumber = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var isLoading = remember { mutableStateOf(false) }
    var passwordVisibility = remember { mutableStateOf(false) }
    var phoneError = remember { mutableStateOf(false) }
    var passwordError = remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Animated Vector
    val drawable =
        remember { AppCompatResources.getDrawable(context, R.drawable.splash_animated_vector) }
    val imageBitmap = remember(drawable) { drawable?.toBitmap()?.asImageBitmap() }

    LaunchedEffect(viewModel.authStatus.collectAsState().value) {
        val authStatus = viewModel.authStatus.value
        if (authStatus != null) {
            isLoading.value = false
            if (authStatus) {
                // Navigate to Home Screen
                Log.d("LoginScreen", "Navigating to Home Screen $authStatus")
                navController.navigate(HomeScreenNavigation.route)
            } else {
                // Show error message
                Log.e("LoginScreen", "Invalid phone number or password")
                Toast.makeText(context, "Invalid phone number or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFF5F5F5))
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated Icon
        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = "Animated Icon",
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Phone Number Input
        OutlinedTextField(
            value = phoneNumber.value,
            onValueChange = {
                phoneNumber.value = it
                phoneError.value = it.length != 11
            },
            label = { Text("Phone Number") },
            leadingIcon = { Icon(painterResource(id = R.drawable.baseline_smartphone_24), contentDescription = "Phone") },
            singleLine = true,
            isError = phoneError.value,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (phoneError.value) Text("Enter a valid 11-digit phone number", color = Color.Red, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
                passwordError.value = it.length < 6
            },
            label = { Text("Password") },
            leadingIcon = { Icon(painterResource(id = R.drawable.baseline_lock_24), contentDescription = "Password") },
            trailingIcon = {
                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    val icon = if (passwordVisibility.value) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                    Icon(painterResource(id = icon), contentDescription = "Toggle Password Visibility")
                }
            },
            singleLine = true,
            isError = passwordError.value,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = if (passwordVisibility.value) KeyboardType.Text else KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if (passwordError.value) Text("Password must be at least 6 characters", color = Color.Red, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                isLoading.value = true
                viewModel.loginUser(
                    phoneNumber.value.toLongOrNull() ?: 0L,
                    password.value
                )
                isLoading.value = false
            },
            enabled = !isLoading.value && phoneNumber.value.isNotEmpty() && password.value.isNotEmpty() && !phoneError.value && !passwordError.value,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Sign-Up Navigation
        TextButton(onClick = { navController.navigate(SignUpNavigation.route) }) {
            Text("Don't have an account? Sign up")
        }
    }

    // Loading Indicator (Full Screen)
    if (isLoading.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
