package com.example.martapp.presentation.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.martapp.data.repository.UserRepository
import com.example.martapp.data.repository.database.User
import com.example.martapp.utils.PasswordUtil
import com.example.martapp.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    // StateFlow to hold the verification ID
    private val _verificationId = MutableStateFlow<String?>(null)
    val verificationId: StateFlow<String?> = _verificationId

    // StateFlow to hold the auth status
    private val _authStatus = MutableStateFlow<Boolean?>(null)
    val authStatus: StateFlow<Boolean?> = _authStatus


    fun signUpUser(
        context: Context,
        name: String,
        phoneNumber: Long,
        password: String,
    ) {
        viewModelScope.launch {

            val existingUser = getUser(phoneNumber)
            if (existingUser != null) {
                _authStatus.value = false
                Log.d("AuthViewModel", "User already exists")
                Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show()
                return@launch
            }
            val hashedPassword = PasswordUtil.hashPassword(password) // Hash the password
            Log.d("AuthViewModel", "Hashed Password: $hashedPassword")
            val newUser = User(
                name = name,
                phoneNumber = phoneNumber,
                password = hashedPassword
            )
            Log.d("AuthViewModel", "New User: $newUser")

            insertUSer(newUser) // Insert the user into the database
            Log.d("AuthViewModel", "User inserted successfully")
            _authStatus.value = true
        }
    }

    fun loginUser(
        phoneNumber: Long,
        password: String,
    ) {
        viewModelScope.launch {
            val user = getUser(phoneNumber)
            if (user != null) {
                val isPasswordCorrect = PasswordUtil.verifyPassword(password, user.password)
                Log.d("AuthViewModel", "Is Password Correct: $isPasswordCorrect")

                if (isPasswordCorrect) {
                    userPreferences.saveUserPhone(phoneNumber)
                    _authStatus.value = true
                    Log.d("AuthViewModel", "User logged in successfully")
                } else {
                    _authStatus.value = false
                    Log.e("AuthViewModel", "Invalid password")
                }
                Log.d("AuthViewModel", "Auth Status: ${_authStatus.value}")
            } else {
                Log.e("AuthViewModel", "User not found")
                _authStatus.value = false
            }
        }
    }


    fun logoutUser() {
        viewModelScope.launch {
            userPreferences.clearSession()
        }
    }


    suspend fun insertUSer(user: User) {
        userRepo.insertUser(user)
    }

    suspend fun getUser(phoneNumber: Long): User? {
        return userRepo.getUserByPhone(phoneNumber)
    }
}