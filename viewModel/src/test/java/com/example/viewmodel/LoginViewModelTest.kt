package com.example.viewmodel

import com.example.domain.useCase.authentication.LoginWithPasswordUseCase
import com.example.viewmodel.login.LoginViewModel
import com.example.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel

    private val testDispatcherProvider = TestDispatcherProvider()
    private var testScope = TestScope(testDispatcherProvider.testDispatcher)
    private val loginWithPasswordUseCase: LoginWithPasswordUseCase = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
        viewModel = LoginViewModel(
            dispatcherProvider = testDispatcherProvider,
            loginWithPasswordUseCase = loginWithPasswordUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onUserNameUpdated should update username`() =
        testScope.runTest {
            val username = "testuser"

            viewModel.onUserNameUpdated(username)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.username).isEqualTo(username)
        }

    @Test
    fun `onPasswordUpdate should update password`() =
        testScope.runTest {
            val password = "testpassword"

            viewModel.onPasswordUpdate(password)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.password).isEqualTo(password)
        }

    @Test
    fun `onUserNameUpdated should clear username errors`() =
        testScope.runTest {

            val username = "testuser"

            viewModel.onUserNameUpdated(username)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.usernameError).isEmpty()
        }

    @Test
    fun `onPasswordUpdate should clear password errors`() =
        testScope.runTest {

            val password = "testpassword"

            viewModel.onPasswordUpdate(password)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.passwordError).isEmpty()
        }

    //TODO: IMPLEMENT TEST
    @Test
    fun `should set username error when login fail happens`() =
        testScope.runTest {

        }

    //TODO: IMPLEMENT TEST
    @Test
    fun `should set password error when login fail happens`() =
        testScope.runTest {

        }

    @Test
    fun `onShowPasswordClicked should toggle password visibility when called`() =
        testScope.runTest {
            val initialState = viewModel.state.value.isPasswordShown

            viewModel.onShowPasswordClicked()
            advanceUntilIdle()

            assertThat(viewModel.state.value.isPasswordShown).isEqualTo(!initialState)

            viewModel.onShowPasswordClicked()
            advanceUntilIdle()

            assertThat(viewModel.state.value.isPasswordShown).isEqualTo(initialState)
        }

    @Test
    fun `login button should be enabled when both username and password are not blank`() =
        testScope.runTest {
            viewModel.onUserNameUpdated("testuser")
            viewModel.onPasswordUpdate("testpassword")
            testScheduler.advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isTrue()
        }

    @Test
    fun `login button should be disabled when username is blank`() =
        testScope.runTest {
            viewModel.onUserNameUpdated("")
            viewModel.onPasswordUpdate("testpassword")
            testScheduler.advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }

    @Test
    fun `login button should be disabled when password is blank`() =
        testScope.runTest {
            viewModel.onUserNameUpdated("testuser")
            viewModel.onPasswordUpdate("")
            testScheduler.advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }

    @Test
    fun `login button should be disabled when both username and password are blank`() =
        testScope.runTest {
            viewModel.onUserNameUpdated("")
            viewModel.onPasswordUpdate("")
            testScheduler.advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }

    @Test
    fun `clearing username should disable login button when password is set`() =
        testScope.runTest {


            viewModel.onUserNameUpdated("")
            testScheduler.advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }

    @Test
    fun `clearing password should disable login button when username is set`() =
        testScope.runTest {


            viewModel.onPasswordUpdate("")
            testScheduler.advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }


    @Test
    fun `onLoginClicked should set loading to false after operation regardless of outcome`() =
        testScope.runTest {
            viewModel.onLoginClicked()
            testScheduler.advanceUntilIdle()

            val result = viewModel.state.value.isLoginButtonLoading
            assertThat(result).isFalse()
        }

    //TODO: Write test case when function is implemented
    @Test
    fun `onLoginClicked should send NavigateToHome effect when login is successful`() =
        testScope.runTest {
        }

    //TODO: Write test case when function is implemented
    @Test
    fun `onLoginClicked should set username and password errors when login fails`() =
        testScope.runTest {

        }

    //TODO: Write test case when function is implemented
    @Test
    fun `onContinueAsGuestClicked should send NavigateToHome effect after success`() =
        testScope.runTest {

        }
}