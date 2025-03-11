package com.example.martapp.data.repository.database.cart

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartItemEntity::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}