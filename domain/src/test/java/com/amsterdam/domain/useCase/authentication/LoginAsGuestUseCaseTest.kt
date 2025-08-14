package com.amsterdam.domain.useCase.authentication

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class LoginAsGuestUseCaseTest {

    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private val loginAsGuestUseCase by lazy {
        LoginAsGuestUseCase(authenticationRepository)
    }


    @Test
    fun `should call setSessionType when login as a GUEST`() = runTest {
        loginAsGuestUseCase()
        coVerify(exactly = 1) { authenticationRepository.setSessionType(SessionType.GUEST) }
    }
}