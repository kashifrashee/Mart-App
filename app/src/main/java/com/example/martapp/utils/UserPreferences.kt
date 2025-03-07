package com.example.martapp.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// DataStore extension for Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    companion object {
        // Keys
        val USER_PHONE = longPreferencesKey("user_phone")
    }

    val userPhone: Flow<Long?> = dataStore.data.map { preferences ->
        Log.d("UserPreferences", "User Phone: ${preferences[USER_PHONE]}")
        preferences[USER_PHONE]
    }

    suspend fun saveUserPhone(phone: Long) {
        dataStore.edit { preferences ->
            preferences[USER_PHONE] = phone
            Log.d("UserPreferences", "User Phone Saved: $phone")
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
            Log.d("UserPreferences", "Session Cleared")
        }
    }
}
