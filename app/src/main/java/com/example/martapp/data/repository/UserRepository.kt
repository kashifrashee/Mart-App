package com.example.martapp.data.repository

import com.example.martapp.data.repository.database.user.User
import com.example.martapp.data.repository.database.user.UserDao
import com.example.martapp.utils.PasswordUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao){

    suspend fun insertUser(user: User){
        userDao.insertUser(user)
    }

    suspend fun getUserByPhone(phoneNumber: Long): User?{
        return withContext(Dispatchers.IO){
            userDao.getUserByPhone(phoneNumber)
        }
    }

    suspend fun updateUserProfile(userId: Int, newName: String, newPhone: Long) {
        withContext(Dispatchers.IO) {
            userDao.updateUserProfile(userId, newName, newPhone)
        }
    }

    suspend fun updatePassword(userId: Int, newPassword: String) {
        val hashedPassword = PasswordUtil.hashPassword(newPassword) // Securely hash new password
        withContext(Dispatchers.IO) {
            userDao.updatePassword(userId, hashedPassword)
        }
    }


}