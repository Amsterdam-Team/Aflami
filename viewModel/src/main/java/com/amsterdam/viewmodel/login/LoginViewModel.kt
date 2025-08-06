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
            it.copy(
                username = username,
                loginError = null,
                isLoginButtonEnabled = username.isNotBlank() && state.value.password.isNotBlank()
            )
        }
    }

    override fun onPasswordUpdate(password: String) {
        updateState {
            it.copy(
                password = password,
                loginError = null,
                isLoginButtonEnabled = password.isNotBlank() && state.value.username.isNotBlank()
            )
        }
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
            onError = ::onError,
            onCompletion = ::onLoginComplete

        )
    }

    override fun onContinueAsGuestClicked() {
        tryToExecute(
            action = { onLoginAsGuestStart() },
            onSuccess = { onLoginSuccess() },
            onError = ::onError,
            onCompletion = ::onLoginComplete
        )
    }

    override fun onForgotPasswordClicked() {
        sendNewNavigationEffect(LoginEffect.NavigateToResetPassword)
    }

    override fun onCreateAccountClicked() {
        sendNewNavigationEffect(LoginEffect.NavigateToRegister)
    }

    private suspend fun onLoginAsGuestStart() {
        updateState { it.copy(isLoginButtonLoading = true) }
        loginAsGuestUseCase()
    }

    private suspend fun onLoginStart() {
        updateState { it.copy(isLoginButtonLoading = true) }
        loginWithPasswordUseCase.invoke(state.value.username, state.value.password)
    }

    private fun onLoginSuccess() {
        sendNewNavigationEffect(LoginEffect.NavigateToHome)
    }

    private fun onError(exception: AflamiException) {
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