package com.example.martapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.martapp.data.repository.UserRepository
import com.example.martapp.utils.PasswordUtil
import com.example.martapp.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userPhone = MutableStateFlow("")
    val userPhone: StateFlow<String> = _userPhone.asStateFlow()

    val isDarkMode = userPreferences.darkModeFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )


    fun logoutUser() {
        viewModelScope.launch {
            userPreferences.clearSession()
        }
    }

    fun updateProfile(userId: Int, newName: String, newPhone: String) {
        viewModelScope.launch {
            if (newName.isBlank() || newPhone.length != 11 || !newPhone.all { it.isDigit() }) {
                Log.e("Update Profile", "Invalid input")
                return@launch
            }
            userRepository.updateUserProfile(userId, newName, newPhone.toLongOrNull() ?: 0L)
            _userName.value = newName
            _userPhone.value = newPhone
            Log.d("Update Profile", "Profile updated successfully!")
        }
    }

    fun changePassword(
        userId: Int,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            val user = userRepository.getUserByPhone(userId.toLong()) ?: return@launch
            if (!PasswordUtil.verifyPassword(oldPassword, user.password)) {
                Log.e("Change Password", "Old password is incorrect!")
                return@launch
            }
            if (newPassword != confirmPassword) {
                Log.e("Change Password", "Passwords do not match!")
                return@launch
            }
            userRepository.updatePassword(userId, newPassword)
            Log.d("Change Password", "Password changed successfully!")
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setDarkMode(enabled)
        }
    }

}