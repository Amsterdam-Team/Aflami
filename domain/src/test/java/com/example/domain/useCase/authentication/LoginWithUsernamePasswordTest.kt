package com.example.domain.useCase.authentication

import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.AuthenticationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LoginWithUsernamePasswordTest {
    private lateinit var loginWithPasswordUseCase: LoginWithPasswordUseCase
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk(relaxed = true)
        loginWithPasswordUseCase = LoginWithPasswordUseCase(authenticationRepository)
    }

    @Test
    fun `should call loginWithPassword with correct parameters`() =
        runTest {
            val username = "testuser"
            val password = "testpassword"

            loginWithPasswordUseCase(username, password)

            coVerify { authenticationRepository.loginWithPassword(username, password) }
        }

    @Test
    fun `should throw AflamiException when loginWithPassword fails`() =
        runTest {
            val username = "testuser"
            val password = "testpassword"

            coEvery {
                authenticationRepository.loginWithPassword(
                    username,
                    password
                )
            } throws AflamiException()

            assertThrows<AflamiException> {
                loginWithPasswordUseCase(username, password)
            }

            coVerify(exactly = 1) { authenticationRepository.loginWithPassword(username, password) }
        }
}
