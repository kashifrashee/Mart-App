package com.example.martapp.data.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("select * from user where phoneNumber = :phoneNumber limit 1")
    suspend fun getUserByPhone(phoneNumber: Long): User?
}