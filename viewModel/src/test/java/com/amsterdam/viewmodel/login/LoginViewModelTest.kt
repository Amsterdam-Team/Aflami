package com.amsterdam.viewmodel.login

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.authentication.LoginAsGuestUseCase
import com.amsterdam.domain.useCase.authentication.LoginWithPasswordUseCase
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var loginAsGuestUseCase: LoginAsGuestUseCase
    private val loginWithPasswordUseCase: LoginWithPasswordUseCase = mockk(relaxed = true)
    private val testDispatcherProvider = TestDispatcherProvider()
    private val testScope = TestScope(testDispatcherProvider.testDispatcher)

    @BeforeEach
    fun setUp() {
        loginAsGuestUseCase = mockk(relaxed = true)
        viewModel = LoginViewModel(
            dispatcherProvider = testDispatcherProvider,
            loginWithPasswordUseCase = loginWithPasswordUseCase,
            loginAsGuestUseCase = loginAsGuestUseCase
        )
    }

    @Test
    fun `onUserNameUpdated should update username`() = testScope.runTest {
        // Given
        val username = "testuser"

        // When
        viewModel.onUserNameUpdated(username)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.username).isEqualTo(username)
    }

    @Test
    fun `onPasswordUpdate should update password`() = testScope.runTest {
        // Given
        val password = "testpassword"

        // When
        viewModel.onPasswordUpdate(password)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.password).isEqualTo(password)
    }

    @Test
    fun `onUserNameUpdated should clear username errors`() = testScope.runTest {
        // Given
        val username = "testuser"

        // When
        viewModel.onUserNameUpdated(username)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.loginError).isNull()
    }

    @Test
    fun `onPasswordUpdate should clear password errors`() = testScope.runTest {
        // Given
        val password = "testpassword"

        // When
        viewModel.onPasswordUpdate(password)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.loginError).isNull()
    }

    @Test
    fun `should set username error when login fail happens`() = testScope.runTest {
        // When
        viewModel.onUserNameUpdated("testuser")
        viewModel.onLoginClicked()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.loginError).isNull()
    }

    @Test
    fun `should set password error when login fail happens`() = testScope.runTest {
        // When
        viewModel.onPasswordUpdate("testpassword")
        viewModel.onLoginClicked()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.loginError).isNull()
    }

    @Test
    fun `onShowPasswordClicked should toggle password visibility when called`() = testScope.runTest {
        // Given
        val initialState = viewModel.state.value.isPasswordShown

        // When
        viewModel.onShowPasswordClicked()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isPasswordShown).isEqualTo(!initialState)

        // When again
        viewModel.onShowPasswordClicked()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isPasswordShown).isEqualTo(initialState)
    }

    @Test
    fun `onUserNameUpdated should set login button to be enabled when both username and password are not blank`() = testScope.runTest {
        // Given
        viewModel.onPasswordUpdate("testpassword")

        // When
        viewModel.onUserNameUpdated("testuser")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoginButtonEnabled).isTrue()
    }

    @Test
    fun `onUserNameUpdated should set login button to be non enabled when username is blank`() = testScope.runTest {
        // When
        viewModel.onUserNameUpdated("")
        viewModel.onPasswordUpdate("testpassword")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoginButtonEnabled).isFalse()
    }

    @Test
    fun `login button should be disabled when username is blank`() = testScope.runTest {
        // When
        viewModel.onUserNameUpdated("")
        viewModel.onPasswordUpdate("testpassword")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoginButtonLoading).isFalse()
    }

    @Test
    fun `login button should be disabled when password is blank`() = testScope.runTest {
        // When
        viewModel.onUserNameUpdated("testuser")
        viewModel.onPasswordUpdate("")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoginButtonLoading).isFalse()
    }

    @Test
    fun `login button should be disabled when both username and password are blank`() = testScope.runTest {
        // When
        viewModel.onUserNameUpdated("")
        viewModel.onPasswordUpdate("")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoginButtonLoading).isFalse()
    }

    @Test
    fun `clearing username should disable login button when password is set`() = testScope.runTest {
        // When
        viewModel.onUserNameUpdated("")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoginButtonLoading).isFalse()
    }

    @Test
    fun `clearing password should disable login button when username is set`() = testScope.runTest {
        // When
        viewModel.onPasswordUpdate("")
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoginButtonLoading).isFalse()
    }

    @Test
    fun `onLoginClicked should set loading to false after operation regardless of outcome`() = testScope.runTest {
        // Given
        coEvery { loginWithPasswordUseCase(any(), any()) } throws AflamiException()

        // When
        viewModel.onLoginClicked()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoginButtonLoading).isFalse()
    }

    @Test
    fun `onLoginClicked should send NavigateToHome effect when login is successful`() = testScope.runTest {
        // Given
        val effects = mutableListOf<LoginEffect>()
        val job = launch { viewModel.effect.collect { effects.add(it) } }

        // When
        viewModel.onLoginClicked()
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(effects).contains(LoginEffect.NavigateToHome)
    }

    @Test
    fun `onContinueAsGuestClicked should send NavigateToHome effect after success`() = testScope.runTest {
        // Given
        val effects = mutableListOf<LoginEffect>()
        val job = launch { viewModel.effect.collect { effects.add(it) } }

        // When
        viewModel.onContinueAsGuestClicked()
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(effects).contains(LoginEffect.NavigateToHome)
    }

    @Test
    fun `onForgotPasswordClicked should send NavigateToResetPassword effect`() = testScope.runTest {
        // Given
        val effects = mutableListOf<LoginEffect>()
        val job = launch { viewModel.effect.collect { effects.add(it) } }

        // When
        viewModel.onForgotPasswordClicked()
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(effects).contains(LoginEffect.NavigateToResetPassword)
    }

    @Test
    fun `onCreateAccountClicked should send NavigateToRegister effect`() = testScope.runTest {
        // Given
        val effects = mutableListOf<LoginEffect>()
        val job = launch { viewModel.effect.collect { effects.add(it) } }

        // When
        viewModel.onCreateAccountClicked()
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(effects).contains(LoginEffect.NavigateToRegister)
    }

    @Test
    fun `onLoginClicked should send ShowCredentialsError effect when login fails`() = testScope.runTest {
        // Given
        coEvery { loginWithPasswordUseCase(any(), any()) } throws AflamiException()
        val effects = mutableListOf<LoginEffect>()
        val job = launch { viewModel.effect.collect { effects.add(it) } }

        // When
        viewModel.onLoginClicked()
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(effects).contains(LoginEffect.ShowCredentialsError)
    }

    @Test
    fun `onContinueAsGuestClicked should update error state when login fails`() = testScope.runTest {
        // Given
        coEvery { loginAsGuestUseCase() } throws AflamiException()

        // When
        viewModel.onContinueAsGuestClicked()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.loginError).isNotNull()
    }
}
