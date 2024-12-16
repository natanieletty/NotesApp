package ua.nuop.bushniak.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val darkThemeKey = booleanPreferencesKey("DARK_THEME")

    suspend fun isDarkTheme(): Boolean? {
        return dataStore.data.first()[darkThemeKey]
    }

    suspend fun setTheme(theme: Boolean) {
        dataStore.edit {
            it[darkThemeKey] = theme
        }
    }

}