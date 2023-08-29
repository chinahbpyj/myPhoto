package com.pyj.myphoto.data.app

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.pyj.myphoto.dataStore
import com.pyj.myphoto.util.CACHE_KEY_DARK_THEME
import com.pyj.myphoto.util.CACHE_KEY_ROW
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppImpl(val context: Context) : AppRepository {
    override suspend fun userData(default: Boolean): UserData {
        return context.dataStore.data
            .map { preferences ->
                UserData(
                    darkTheme = preferences[booleanPreferencesKey(CACHE_KEY_DARK_THEME)] ?: default,
                    row = preferences[booleanPreferencesKey(CACHE_KEY_ROW)] ?: default
                )
            }.first()
    }
}