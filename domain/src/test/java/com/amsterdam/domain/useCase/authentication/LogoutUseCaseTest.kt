package com.amsterdam.domain.useCase.authentication

import com.amsterdam.domain.repository.AuthenticationRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogoutUseCaseTest {
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var logoutUseCase: LogoutUseCase
    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk()
        logoutUseCase = LogoutUseCase(authenticationRepository)
    }
    @Test
    fun `should call logout on authentication repository`() = runTest {
        // Given
        coEvery { authenticationRepository.logout() } just Runs

        // When
        logoutUseCase()

        // Then
        coVerify(exactly = 1) { authenticationRepository.logout() }
    }


}