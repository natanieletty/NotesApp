package ua.nuop.bushniak.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nuop.bushniak.repo.PrivatePasswordRepo
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val passwordRepo: PrivatePasswordRepo) :
    ViewModel() {
    private val _isPasswordSet = MutableStateFlow(true)
    private val _password = MutableStateFlow("")
    private val _isPasswordValid = MutableStateFlow<Boolean?>(null)
//    private va

    val isPasswordValid = _isPasswordValid.asStateFlow()
    val isPasswordSet = _isPasswordSet.asStateFlow()
    val password = _password.asStateFlow()


    init {
        viewModelScope.launch {
            _isPasswordSet.value = passwordRepo.isPasswordSet()
        }
    }


    fun updatePasswordField(pass: String) {
        _password.update {
            pass
        }
    }

    fun validatePassword() {
        viewModelScope.launch {
            if (passwordRepo.verifyPassword(_password.value)) {
                _isPasswordValid.update {
                    true
                }
            } else {
                _isPasswordValid.update {
                    false
                }
            }

        }
    }


}