package com.amsterdam.domain.useCase.authentication

import com.amsterdam.domain.repository.AuthenticationRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class LogoutUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val logoutUseCase by lazy {
        LogoutUseCase(authenticationRepository)
    }

    @Test
    fun `should call logout on authentication repository`() = runTest {
        coEvery { authenticationRepository.logout() } just Runs

        logoutUseCase()

        coVerify(exactly = 1) { authenticationRepository.logout() }
    }
}