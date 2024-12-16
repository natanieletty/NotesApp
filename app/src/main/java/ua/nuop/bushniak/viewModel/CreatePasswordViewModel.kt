package ua.nuop.bushniak.viewModel

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
class CreatePasswordViewModel @Inject constructor(private val passwordRepo: PrivatePasswordRepo) :
    ViewModel() {
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun updatePassword(newPassword: String) {
        _password.update {
            newPassword
        }
    }

    fun setPassword() {
        viewModelScope.launch {
            passwordRepo.setPassword(_password.value)
        }
    }
}