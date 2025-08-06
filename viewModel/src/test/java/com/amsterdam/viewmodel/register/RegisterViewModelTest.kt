package com.amsterdam.viewmodel.register

import com.amsterdam.viewmodel.BuildConfig
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private lateinit var viewModel: RegisterViewModel

    private val testDispatcherProvider = TestDispatcherProvider()
    private var testScope = TestScope(testDispatcherProvider.testDispatcher)

    @BeforeEach
    fun setUp() {
        viewModel = RegisterViewModel(
            dispatcherProvider = testDispatcherProvider
        )
    }
    @Test
    fun `init should set signUpUrl to BuildConfig value when called`() = testScope.runTest{
        //Given && When
        viewModel = RegisterViewModel(
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()
        //Then
        val signUpUrl = viewModel.state.value.signUpUrl
        assertThat(signUpUrl).isEqualTo(BuildConfig.MOVIE_SIGN_UP_URL)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `setLoading should update isLoginButtonLoading state`(loading: Boolean) = testScope.runTest {
        // When
        viewModel.setLoading(loading)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoading).isEqualTo(loading)
    }

    @Test
    fun `onRegistrationComplete should send NavigateToSignIn effect when its call`() = testScope.runTest {
        // Given
        val effects = mutableListOf<RegisterEffect>()
        val job = launch { viewModel.effect.collect { effects.add(it) } }

        // When
        viewModel.onRegistrationComplete()
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(effects).contains(RegisterEffect.NavigateToSignIn)
    }

}