package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.AccessDeniedException
import com.amsterdam.domain.exceptions.AccessRestrictedException
import com.amsterdam.domain.exceptions.AccountDisabledException
import com.amsterdam.domain.exceptions.InvalidCredentialsException
import com.amsterdam.domain.exceptions.InvalidSessionException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.exceptions.ServerErrorException
import com.amsterdam.domain.exceptions.VerificationRequiredException
import com.amsterdam.remotedatasource.api.AuthenticationApiService
import com.amsterdam.repository.dto.remote.authentication.AuthenticationRemoteResponse
import com.amsterdam.repository.dto.remote.authentication.CreateSessionRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException

class AuthenticationRemoteDataSourceImplTest {

    private val json: Json = Json { ignoreUnknownKeys = true }
    private val authenticationApiService: AuthenticationApiService = mockk()
    private val authenticationRemoteDataSourceImpl: AuthenticationRemoteDataSourceImpl =
        AuthenticationRemoteDataSourceImpl(json, authenticationApiService)

    @Test
    fun `loginWithPassword should return the correct session ID on success`() = runTest {
        mockSuccessfulLoginFlow()

        val result = authenticationRemoteDataSourceImpl.loginWithPassword(username, password)

        assertThat(result).isEqualTo(sessionId)
    }

    @Test
    fun `createRequestToken is called exactly once when logging in with a password`() = runTest {
        mockSuccessfulLoginFlow()

        authenticationRemoteDataSourceImpl.loginWithPassword(username, password)

        coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
    }

    @Test
    fun `createSessionWithLogin is called exactly once when logging in with a password`() =
        runTest {
            mockSuccessfulLoginFlow()

            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)

            coVerify(exactly = 1) { authenticationApiService.createSessionWithLogin(any()) }
        }

    @Test
    fun `createSession is called exactly once when logging in with a password`() = runTest {
        mockSuccessfulLoginFlow()

        authenticationRemoteDataSourceImpl.loginWithPassword(username, password)

        coVerify(exactly = 1) { authenticationApiService.createSession(any()) }
    }

    @Test
    fun `createRequestToken should throw NoInternetException on ConnectException`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } throws connectException

        assertThrows<NoInternetException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `createRequestToken should throw ServerErrorException on SerializationException`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } throws serializationException

            assertThrows<ServerErrorException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createRequestToken should throw ServerErrorException on generic HttpException`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } throws httpException500

            assertThrows<ServerErrorException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createRequestToken should throw NetworkException on generic Exception`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } throws genericException

        assertThrows<NetworkException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `loginWithPassword should throw a NetworkException and call createRequestToken exactly once if the request token is null`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse.copy(
                requestToken = null
            )

            assertThrows<NetworkException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }

            coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
        }

    @Test
    fun `createSessionWithLogin should throw NoInternetException on ConnectException`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } throws connectException

        assertThrows<NoInternetException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `createSessionWithLogin should throw ServerErrorException on SerializationException`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws serializationException

            assertThrows<ServerErrorException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw InvalidCredentialsException on 401 with code 3`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws invalidCredentialsException

            assertThrows<InvalidCredentialsException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw InvalidSessionException on 401 with code 7`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws invalidSessionException

            assertThrows<InvalidSessionException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw AccountDisabledException on 401 with code 31`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws accountDisabledException

            assertThrows<AccountDisabledException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw VerificationRequiredException on 401 with code 32`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws verificationRequiredException

            assertThrows<VerificationRequiredException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw AccessDeniedException on 401 with code 38`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws accessDeniedException

            assertThrows<AccessDeniedException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw AccessRestrictedException on 401 with code 45`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws accessRestrictedException

            assertThrows<AccessRestrictedException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw NetworkException on 401 with unknown code`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws unknown401Exception

            assertThrows<NetworkException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw ServerErrorException on generic HttpException`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws httpException500

            assertThrows<ServerErrorException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
        }

    @Test
    fun `createSessionWithLogin should throw NetworkException on generic Exception`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } throws genericException

        assertThrows<NetworkException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `loginWithPassword should throw a NetworkException and call createRequestToken exactly once if the request token is null after calling createSessionWithLogin`() =
        runTest {
            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse.copy(
                requestToken = null
            )

            assertThrows<NetworkException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }

            coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
        }

    @Test
    fun `createSession should throw NoInternetException on ConnectException`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(any()) } throws connectException

        assertThrows<NoInternetException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `createSession should throw ServerErrorException on SerializationException`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(any()) } throws serializationException

        assertThrows<ServerErrorException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `createSession should throw ServerErrorException on generic HttpException`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(any()) } throws httpException500

        assertThrows<ServerErrorException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    @Test
    fun `createSession should throw NetworkException on generic Exception`() = runTest {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(any()) } throws genericException

        assertThrows<NetworkException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
    }

    private val username = "testUser"
    private val password = "testPassword"
    private val requestToken = "unactive_request_token"
    private val activeRequestToken = "active_request_token"
    private val sessionId = "final_session_id"

    private val unactiveTokenResponse = AuthenticationRemoteResponse(
        isSuccess = true,
        expiresAt = "2025-08-04",
        requestToken = requestToken,
        statusCode = 200
    )
    private val activeTokenResponse = AuthenticationRemoteResponse(
        isSuccess = true,
        expiresAt = "2025-08-04",
        requestToken = activeRequestToken,
        statusCode = 200
    )
    private val createSessionResponse = CreateSessionRemoteResponse(
        success = true,
        sessionId = sessionId
    )

    private fun mockSuccessfulLoginFlow() {
        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(any()) } returns createSessionResponse
    }

    private val connectException = ConnectException()
    private val serializationException = SerializationException()
    private val genericException = Exception("Generic error")

    private val httpException500 = HttpException(Response.error<Any>(500, "".toResponseBody()))

    private val invalidCredentialsErrorBody =
        "{\"status_code\": 3, \"status_message\": \"Invalid credentials\"}"
    private val invalidCredentialsException = HttpException(
        Response.error<Any>(
            401,
            invalidCredentialsErrorBody.toResponseBody("application/json".toMediaTypeOrNull())
        )
    )

    private val invalidSessionErrorBody =
        "{\"status_code\": 7, \"status_message\": \"Invalid session\"}"
    private val invalidSessionException = HttpException(
        Response.error<Any>(
            401,
            invalidSessionErrorBody.toResponseBody("application/json".toMediaTypeOrNull())
        )
    )

    private val accountDisabledErrorBody =
        "{\"status_code\": 31, \"status_message\": \"Account disabled\"}"
    private val accountDisabledException = HttpException(
        Response.error<Any>(
            401,
            accountDisabledErrorBody.toResponseBody("application/json".toMediaTypeOrNull())
        )
    )

    private val verificationRequiredErrorBody =
        "{\"status_code\": 32, \"status_message\": \"Verification required\"}"
    private val verificationRequiredException = HttpException(
        Response.error<Any>(
            401,
            verificationRequiredErrorBody.toResponseBody("application/json".toMediaTypeOrNull())
        )
    )

    private val accessDeniedErrorBody =
        "{\"status_code\": 38, \"status_message\": \"Access denied\"}"
    private val accessDeniedException = HttpException(
        Response.error<Any>(
            401,
            accessDeniedErrorBody.toResponseBody("application/json".toMediaTypeOrNull())
        )
    )

    private val accessRestrictedErrorBody =
        "{\"status_code\": 45, \"status_message\": \"Access restricted\"}"
    private val accessRestrictedException = HttpException(
        Response.error<Any>(
            401,
            accessRestrictedErrorBody.toResponseBody("application/json".toMediaTypeOrNull())
        )
    )

    private val unknown401ErrorBody =
        "{\"status_code\": 999, \"status_message\": \"Unknown error\"}"
    private val unknown401Exception = HttpException(
        Response.error<Any>(
            401,
            unknown401ErrorBody.toResponseBody("application/json".toMediaTypeOrNull())
        )
    )
}