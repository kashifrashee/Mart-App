package com.example.martapp.data.repository.database.favorite

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteProductEntity::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}