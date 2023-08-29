package com.pyj.myphoto.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pyj.myphoto.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//val exampleData = runBlocking { context.dataStore.data.first() }

/*   You should also handle IOExceptions here.
lifecycleScope.launch {
    context.dataStore.data.first()
}*/

//Int
suspend fun setIntValue(context: Context, keyName: String, value: Int) {
    val key = intPreferencesKey(keyName)

    context.dataStore.edit { settings ->
        settings[key] = value
    }
}

fun getIntValue(context: Context, keyName: String, default: Int = 0): Flow<Int> {
    val key = intPreferencesKey(keyName)

    val value: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[key] ?: default
        }

    return value
}

//String
suspend fun setStringValue(context: Context, keyName: String, value: String) {
    val key = stringPreferencesKey(keyName)

    context.dataStore.edit { settings ->
        settings[key] = value
    }
}

fun getStringValue(context: Context, keyName: String, default: String = ""): Flow<String> {
    val key = stringPreferencesKey(keyName)

    val value: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[key] ?: default
        }

    return value
}

//Boolean
suspend fun setBooleanValue(context: Context, keyName: String, value: Boolean) {
    val key = booleanPreferencesKey(keyName)

    context.dataStore.edit { settings ->
        settings[key] = value
    }
}

fun getBooleanValue(context: Context, keyName: String, default: Boolean = false): Flow<Boolean> {
    val key = booleanPreferencesKey(keyName)

    val value: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[key] ?: default
        }

    return value
}