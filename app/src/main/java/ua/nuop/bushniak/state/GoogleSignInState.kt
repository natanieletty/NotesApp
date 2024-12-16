package ua.nuop.bushniak.state

import com.google.firebase.auth.AuthResult


data class GoogleSignInState(
    val success: AuthResult? = null,
    val loading: Boolean = false,
    val error: String = ""
)