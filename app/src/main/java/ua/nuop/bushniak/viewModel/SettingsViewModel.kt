package ua.nuop.bushniak.viewModel

import android.service.credentials.GetCredentialRequest
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nuop.bushniak.data.firestore.FirestoreClient
import ua.nuop.bushniak.helper.maskEmail
import ua.nuop.bushniak.repo.AuthRepo
import ua.nuop.bushniak.repo.PrivatePasswordRepo
import ua.nuop.bushniak.repo.ThemeRepo
import ua.nuop.bushniak.state.GoogleSignInState
import ua.nuop.bushniak.util.Resource
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeRepo: ThemeRepo,
    private val authRepo: AuthRepo,
    private val privatePasswordRepo: PrivatePasswordRepo
) : ViewModel() {
    private val _authState = MutableStateFlow(GoogleSignInState())
    private val _email = MutableStateFlow("")
    private val _isPasswordSet = MutableStateFlow(false)
    private val _isPasswordUpdatedSuccessfully = MutableStateFlow<Boolean?>(null)


    val authState = _authState.asStateFlow()
    val email = _email.asStateFlow()
    val isPasswordSet = _isPasswordSet.asStateFlow()
    val isPasswordUpdatedSuccessfully = _isPasswordUpdatedSuccessfully.asStateFlow()


    fun toggleScheme() {
        viewModelScope.launch {
            themeRepo.toggleScheme()
        }
    }


    init {
        val user = authRepo.getCurrentUser()
        if (user != null) {
            val email = user.email ?: ""
            _email.value = maskEmail(email)
        }

        viewModelScope.launch {
            _isPasswordSet.value = privatePasswordRepo.isPasswordSet()

        }

    }

    fun googleLogin(authCredential: AuthCredential) {
        viewModelScope.launch {
            authRepo.googleLogin(authCredential).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _authState.update {
                            GoogleSignInState(result.data)
                        }
                    }

                    is Resource.Loading -> {
                        GoogleSignInState(loading = true)
                    }

                    is Resource.Error -> {
                        GoogleSignInState(error = result.message ?: "")
                    }
                }
            }
        }
    }

    fun setPassword(password: String) {
        viewModelScope.launch {
            privatePasswordRepo.setPassword(password)
            _isPasswordSet.value = true
        }
    }

    fun updatePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _isPasswordUpdatedSuccessfully.value =
                privatePasswordRepo.updatePassword(oldPassword, newPassword)
        }
    }

    fun resetIsPasswordUpdatedSuccessfully() {
        _isPasswordUpdatedSuccessfully.value = null
    }

}

