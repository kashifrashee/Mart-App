package com.example.martapp.data.repository

import com.example.martapp.data.repository.database.User
import com.example.martapp.data.repository.database.UserDao
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
}