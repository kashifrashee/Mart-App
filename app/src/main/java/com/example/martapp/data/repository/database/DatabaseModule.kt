package com.example.martapp.data.repository.database

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.martapp.data.repository.UserRepository
import com.example.martapp.data.repository.model.CartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase {
        Log.d("DatabaseModule", "Database created")
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "user_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCartDatabase(@ApplicationContext context: Context): CartDatabase {
        Log.d("DatabaseModule", "CartDatabase created")
        return Room.databaseBuilder(
            context,
            CartDatabase::class.java,
            "cart_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: UserDatabase): UserDao {
        Log.d("DatabaseModule", "UserDao created")
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideCartDao(database: CartDatabase): CartDao {
        Log.d("DatabaseModule", "CartDao created")
        return database.cartDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        Log.d("DatabaseModule", "UserRepository created")
        return UserRepository(userDao)
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao): CartRepository {
        Log.d("DatabaseModule", "CartRepository created")
        return CartRepository(cartDao)
    }

}