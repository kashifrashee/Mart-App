package com.example.martapp.presentation.viewmodel.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.martapp.presentation.viewmodel.SettingsViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.martapp.navigation.NavigationDestination

object EditProfileScreenDestination : NavigationDestination {
    override val route: String = "edit-profile"
    override val title: String = "Edit Profile"
}

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel(),
    userId: Int
) {
    val userName by viewModel.userName.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()

    var name by rememberSaveable { mutableStateOf(userName) }
    var phone by rememberSaveable { mutableStateOf(userPhone) }
    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),

    ) {
        Text(
            text = "Edit Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Name Field
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = it.isBlank()
            },
            label = { Text("Name") },
            isError = nameError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (nameError) {
            Text(text = "Name cannot be empty", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Phone Field
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
                phoneError = it.length < 10 || !it.all { char -> char.isDigit() }
            },
            label = { Text("Phone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = phoneError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (phoneError) {
            Text(text = "Enter a valid phone number", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Changes Button
        Button(
            onClick = {
                if (name.isBlank()) nameError = true
                if (phone.length < 10 || !phone.all { it.isDigit() }) phoneError = true

                if (!nameError && !phoneError) {
                    viewModel.updateProfile(userId, name, phone)
                }
            },

            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save Changes")

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cancel Button
        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cancel")
        }
    }
}


