package ua.nuop.bushniak.repo

import ua.nuop.bushniak.data.datastore.PasswordDatastore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrivatePasswordRepo @Inject constructor(
    private val passwordStorage: PasswordDatastore,
) {

    suspend fun isPasswordSet(): Boolean {
        return passwordStorage.getPassword() != ""
    }

    suspend fun setPassword(password: String): Boolean {
        if (!isPasswordSet()) {
            passwordStorage.setPassword(password)
            return true
        }
        return false
    }

    suspend fun updatePassword(oldPassword: String, newPassword: String): Boolean {
        if (verifyPassword(oldPassword)) {
            passwordStorage.setPassword(newPassword)
            return true
        }
        return false
    }

    suspend fun verifyPassword(password: String): Boolean {
        return passwordStorage.getPassword() == password
    }

}