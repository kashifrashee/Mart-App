package com.example.martapp.presentation.viewmodel.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.martapp.presentation.viewmodel.SettingsViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.martapp.navigation.NavigationDestination


object ThemeSelectionScreenNavigation : NavigationDestination {
    override val route: String = "theme"
    override val title: String = "Theme"
}

@Composable
fun ThemeSelectionScreen(viewModel: SettingsViewModel = hiltViewModel()) {
val isDarkMode = viewModel.isDarkMode.collectAsState(initial = null)
    val backgroundColor by animateColorAsState(
        targetValue = if (isDarkMode.value == true) Color(0xFF121212) else Color.White,
        label = "Background Animation"
    )

    val textColor by animateColorAsState(
        targetValue = if (isDarkMode.value == true) Color.White else Color.Black,
        label = "Text Animation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),

    ) {
        Text(
            text = "Theme Settings",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = textColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = if (isDarkMode.value == true) Color.DarkGray else Color.LightGray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isDarkMode.value == true) "Dark Mode" else "Light Mode",
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor
                )

                Switch(
                    checked = isDarkMode.value == true,
                    onCheckedChange = {
                        viewModel.setDarkMode(it) // Updates global theme state
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Gray,
                        uncheckedThumbColor = Color.DarkGray,
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }
        }
    }
}



