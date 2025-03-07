package com.example.martapp.data.repository.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseAuth {
        Log.d("FirebaseModule", "Providing FirebaseAuth instance")
        return FirebaseAuth.getInstance()
    }
}