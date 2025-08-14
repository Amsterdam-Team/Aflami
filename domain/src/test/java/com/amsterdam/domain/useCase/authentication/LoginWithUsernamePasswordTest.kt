package com.amsterdam.domain.useCase.authentication

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.InvalidCredentialsException
import com.amsterdam.domain.repository.AuthenticationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LoginWithUsernamePasswordTest {
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private val loginWithPasswordUseCase by lazy {
        LoginWithPasswordUseCase(authenticationRepository)
    }

    @Test
    fun `should call loginWithPassword with correct parameters`() =
        runTest {

            loginWithPasswordUseCase(username, password)

            coVerify { authenticationRepository.loginWithPassword(username, password) }
        }

    @Test
    fun `should throw AflamiException when loginWithPassword fails`() =
        runTest {


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

    @Test
    fun `should throw InvalidCredentialsException when credentials are incorrect`() =
        runTest {

            coEvery {
                authenticationRepository.loginWithPassword(
                    wrongUsername,
                    wrongPassword
                )
            } throws InvalidCredentialsException()

            assertThrows<InvalidCredentialsException> {
                loginWithPasswordUseCase(wrongUsername, wrongPassword)
            }

            coVerify(exactly = 1) { authenticationRepository.loginWithPassword(wrongUsername, wrongPassword) }
        }

    private val username = "testuser"
    private val password = "testpassword"

    private val wrongUsername = "wronguser"
    private val wrongPassword = "wrongpassword"

}
