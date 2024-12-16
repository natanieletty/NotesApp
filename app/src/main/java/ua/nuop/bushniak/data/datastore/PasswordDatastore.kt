package ua.nuop.bushniak.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordDatastore @Inject constructor(private val datastore: DataStore<Preferences>) {
    private val PASSWORD = stringPreferencesKey("password")

    suspend fun getPassword(): String {
        return datastore.data.first()[PASSWORD] ?: ""
    }

    suspend fun setPassword(password: String) {
        datastore.edit {
            it[PASSWORD] = password
        }
    }
}