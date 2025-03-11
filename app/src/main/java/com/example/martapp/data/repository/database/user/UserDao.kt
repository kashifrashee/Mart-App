package com.example.martapp.data.repository.database.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("select * from user where phoneNumber = :phoneNumber limit 1")
    suspend fun getUserByPhone(phoneNumber: Long): User?

    @Query("UPDATE user SET name = :newName, phoneNumber = :newPhone WHERE id = :userId")
    suspend fun updateUserProfile(userId: Int, newName: String, newPhone: Long)

    @Query("UPDATE user SET password = :newPassword WHERE id = :userId")
    suspend fun updatePassword(userId: Int, newPassword: String)


}