package com.example.viewmodel.login

import androidx.lifecycle.viewModelScope
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import kotlin.random.Random

class LoginViewModel : BaseViewModel<LoginUiState, LoginEffect>, LoginInteractionListener {

    constructor(dispatcherProvider: DispatcherProvider): super(dispatcherProvider = dispatcherProvider, initialState = LoginUiState())

    private constructor(dispatcherProvider: DispatcherProvider, loginUiState: LoginUiState): super(loginUiState, dispatcherProvider)

    override fun onUserNameUpdated(username: String) {
        updateState {
            it.copy(username = username, usernameError = "")
        }
        shouldEnableLoginButton()
    }

    override fun onPasswordUpdate(password: String) {
        updateState {
            it.copy(password = password, passwordError = "")
        }
        shouldEnableLoginButton()
    }

    override fun onShowPasswordClicked() {
        updateState {
            it.copy(isPasswordShown = !state.value.isPasswordShown)
        }
    }

    override fun onLoginClicked() {
        viewModelScope.launch {
            updateState { it.copy(isLoginButtonLoading = true) }
            delay(3000)
            if (Random.nextBoolean()){
                updateState {
                    it.copy(isLoginButtonLoading = false)
                }
                sendNewEffect(LoginEffect.NavigateToHome)
            } else {
                updateState {
                    it.copy(
                        isLoginButtonLoading = false,
                        usernameError = "Incorrect Username",
                        passwordError = "Incorrect Password"
                    )
                }
            }
        }
    }

    override fun onContinueAsGuestClicked() {
        viewModelScope.launch {
            delay(1000)
            sendNewEffect(LoginEffect.NavigateToHome)
        }
    }

    override fun onForgotPasswordClicked() {

    }

    override fun onCreateAccountClicked() {

    }

    private fun shouldEnableLoginButton(){
        if (state.value.username.isNotBlank() && state.value.password.isNotBlank()){
            updateState {
                it.copy(
                    isLoginButtonEnabled = true
                )
            }
        } else {
            updateState {
                it.copy(isLoginButtonEnabled = false)
            }
        }
    }

    @TestOnly
    companion object{
        fun getViewModel(
            dispatcherProvider: DispatcherProvider,
            loginUiState: LoginUiState
        ): LoginViewModel {
            return LoginViewModel(dispatcherProvider, loginUiState)
        }
    }
}