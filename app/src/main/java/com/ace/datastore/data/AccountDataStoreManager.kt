package com.ace.datastore.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ace.datastore.data.AccountDataStoreManager.Companion.accountDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountDataStoreManager(private val context: Context) {

    suspend fun setAccount(username: String, password: String) {
        context.accountDataStore.edit { preferences ->
            preferences[ACCOUNT_USERNAME] = username
            preferences[ACCOUNT_PASSWORD] = password
        }
    }

    suspend fun setLoginInfo(loginInfo: Boolean) {
        context.accountDataStore.edit { preferences ->
            preferences[LOGGED_IN_KEY] = loginInfo
        }
    }

    fun getAccount(): Flow<Prefs> {
        return context.accountDataStore.data.map { preferences ->
            Prefs(
                preferences[ACCOUNT_USERNAME] ?: "",
                preferences[ACCOUNT_PASSWORD] ?: "",
                preferences[LOGGED_IN_KEY] ?: false)
        }
    }


    fun getLoginInfo(): Flow<Boolean> {
        return context.accountDataStore.data.map {preferences ->
            preferences[LOGGED_IN_KEY] ?: false
        }
    }

    companion object {
        private const val DATASTORE_NAME = "account_preferences"

        private val ACCOUNT_USERNAME = stringPreferencesKey("account_username")

        private val ACCOUNT_PASSWORD = stringPreferencesKey("account_password")

        private val Context.accountDataStore by preferencesDataStore(name = DATASTORE_NAME)

        private val LOGGED_IN_KEY = booleanPreferencesKey("logged_in_key")
    }
}