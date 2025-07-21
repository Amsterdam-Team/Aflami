package com.example.viewmodel.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoginButtonLoading: Boolean = false,
    val isLoginButtonEnabled: Boolean = false,
    val usernameError: String = "",
    val passwordError: String = "",
    val isPasswordShown: Boolean = false
)
