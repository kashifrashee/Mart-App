package com.example.martapp.utils

import android.util.Log
import org.mindrot.jbcrypt.BCrypt

object PasswordUtil {

    // Hash the password using BCrypt
    fun hashPassword(password: String): String {
        Log.d("PasswordUtil", "hashPassword: $password")
        return BCrypt.hashpw(password, BCrypt.gensalt(12)) // 12 rounds of hashing
    }

    // Check if the password matches the hashed password
    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        Log.d("PasswordUtil", "checkPassword: $password, $hashedPassword")
        return BCrypt.checkpw(password, hashedPassword)
    }
}