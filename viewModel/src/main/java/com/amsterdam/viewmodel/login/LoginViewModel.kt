package com.amsterdam.viewmodel.login

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.authentication.LoginAsGuestUseCase
import com.amsterdam.domain.useCase.authentication.LoginWithPasswordUseCase
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val loginWithPasswordUseCase: LoginWithPasswordUseCase,
    private val loginAsGuestUseCase: LoginAsGuestUseCase
) : BaseViewModel<LoginUiState, LoginEffect>(LoginUiState(), dispatcherProvider),
    LoginInteractionListener {

    override fun onUserNameUpdated(username: String) {
        updateState {
            it.copy(username = username, loginError = null)
        }
        shouldEnableLoginButton()
    }

    override fun onPasswordUpdate(password: String) {
        updateState {
            it.copy(password = password, loginError = null)
        }
        shouldEnableLoginButton()
    }


    override fun onShowPasswordClicked() {
        updateState {
            it.copy(isPasswordShown = !state.value.isPasswordShown)
        }
    }

    override fun onLoginClicked() {
        tryToExecute(
            action = ::onLoginStart,
            onSuccess = { onLoginSuccess() },
            onError = ::onLoginWithPasswordError,
            onCompletion = ::onLoginComplete

        )
    }

    override fun onContinueAsGuestClicked() {
        tryToExecute(
            action = { onLoginAsGuestStart() },
            onSuccess = { onLoginSuccess() },
            onError = {},
            onCompletion = ::onLoginComplete
        )
    }

    override fun onForgotPasswordClicked() {
        sendNewEffect(LoginEffect.NavigateToResetPassword)
    }

    override fun onCreateAccountClicked() {
        sendNewEffect(LoginEffect.NavigateToRegister)
    }

    private fun shouldEnableLoginButton() {
        if (state.value.username.isNotBlank() && state.value.password.isNotBlank()) {
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

    private suspend fun onLoginAsGuestStart(){
        updateState { it.copy(isLoginButtonLoading = true) }
        loginAsGuestUseCase()
    }

    private suspend fun onLoginStart() {
        updateState { it.copy(isLoginButtonLoading = true) }
        loginWithPasswordUseCase.invoke(state.value.username, state.value.password)
    }

    private fun onLoginSuccess() {
        sendNewEffect(LoginEffect.NavigateToHome)
    }

    private fun onLoginWithPasswordError(exception: AflamiException) {
        updateState {
            it.copy(
                loginError = LoginErrorState.toLoginErrorState(exception),
            )
        }
        sendNewEffect(LoginEffect.ShowCredentialsError)
    }

    private fun onLoginComplete() {
        updateState { it.copy(isLoginButtonLoading = false) }
    }
}