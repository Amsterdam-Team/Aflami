package com.example.viewmodel.utils.viewmodelHelper

import com.example.viewmodel.login.LoginUiState
import com.example.viewmodel.login.LoginViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider

fun createLoginViewModel(
    dispatcherProvider: DispatcherProvider,
    username: String = "",
    password: String = "",
    isLoginButtonLoading: Boolean = false,
    isLoginButtonEnabled: Boolean = false,
    usernameError: String = "",
    passwordError: String = "",
    isPasswordShown: Boolean = false
): LoginViewModel {
    return LoginViewModel.getViewModel(
        loginUiState = LoginUiState(
            username = username,
            password = password,
            isLoginButtonLoading = isLoginButtonLoading,
            isLoginButtonEnabled = isLoginButtonEnabled,
            usernameError = usernameError,
            passwordError = passwordError,
            isPasswordShown = isPasswordShown
        ),
        dispatcherProvider = dispatcherProvider
    )
}