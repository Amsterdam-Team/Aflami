package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.AuthenticationApiService
import com.amsterdam.repository.dto.remote.authentication.AuthenticationResponseDto
import com.amsterdam.repository.dto.remote.authentication.CreateSessionDto
import com.amsterdam.repository.dto.remote.authentication.CreateSessionResponseDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthenticationRemoteDataSourceImplTest {

    private lateinit var authenticationApiService: AuthenticationApiService
    private lateinit var json: Json
    private lateinit var authenticationRemoteDataSourceImpl: AuthenticationRemoteDataSourceImpl

    @BeforeEach
    fun setUp() {
        authenticationApiService = mockk()
        json = Json { ignoreUnknownKeys = true }
        authenticationRemoteDataSourceImpl =
            AuthenticationRemoteDataSourceImpl(json, authenticationApiService)
    }

    @Test
    fun `loginWithPassword should return sessionId when all api calls are successful`() = runTest {
        // Given
        val username = "testUser"
        val password = "testPassword"
        val requestToken = "unactive_request_token"
        val activeRequestToken = "active_request_token"
        val sessionId = "final_session_id"

        val unactiveTokenResponse = AuthenticationResponseDto(
            isSuccess = true,
            expiresAt = "2025-08-04",
            requestToken = requestToken
        )
        val activeTokenResponse = AuthenticationResponseDto(
            isSuccess = true,
            expiresAt = "2025-08-04",
            requestToken = activeRequestToken
        )
        val createSessionResponse = CreateSessionResponseDto(
            success = true,
            sessionId = sessionId
        )

        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery {
            authenticationApiService.createSessionWithLogin(
                CreateSessionDto(
                    username,
                    password,
                    requestToken
                )
            )
        } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(activeRequestToken) } returns createSessionResponse

        // When
        val result = authenticationRemoteDataSourceImpl.loginWithPassword(username, password)

        // Then
        assertThat(result).isEqualTo(sessionId)
        coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
        coVerify(exactly = 1) {
            authenticationApiService.createSessionWithLogin(
                CreateSessionDto(
                    username,
                    password,
                    requestToken
                )
            )
        }
        coVerify(exactly = 1) { authenticationApiService.createSession(activeRequestToken) }
    }

    @Test
    fun `loginWithPassword should throw NetworkException when createRequestToken fails`() =
        runTest {
            // Given
            val username = "testUser"
            val password = "testPassword"
            coEvery { authenticationApiService.createRequestToken() } throws NetworkException()

            // When & Then
            assertThrows<NetworkException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
            coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
            coVerify(exactly = 0) { authenticationApiService.createSessionWithLogin(any()) }
            coVerify(exactly = 0) { authenticationApiService.createSession(any()) }
        }

    @Test
    fun `loginWithPassword should throw NetworkException when createSessionWithLogin fails`() =
        runTest {
            // Given
            val username = "testUser"
            val password = "testPassword"
            val requestToken = "unactive_request_token"
            val unactiveTokenResponse = AuthenticationResponseDto(
                isSuccess = true,
                expiresAt = "2025-08-04",
                requestToken = requestToken
            )

            coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
            coEvery { authenticationApiService.createSessionWithLogin(any()) } throws NetworkException()

            // When & Then
            assertThrows<NetworkException> {
                authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
            }
            coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
            coVerify(exactly = 1) { authenticationApiService.createSessionWithLogin(any()) }
            coVerify(exactly = 0) { authenticationApiService.createSession(any()) }
        }

    @Test
    fun `loginWithPassword should throw NetworkException when createSession fails`() = runTest {
        // Given
        val username = "testUser"
        val password = "testPassword"
        val requestToken = "unactive_request_token"
        val activeRequestToken = "active_request_token"
        val unactiveTokenResponse = AuthenticationResponseDto(
            isSuccess = true,
            expiresAt = "2025-08-04",
            requestToken = requestToken
        )
        val activeTokenResponse = AuthenticationResponseDto(
            isSuccess = true,
            expiresAt = "2025-08-04",
            requestToken = activeRequestToken
        )

        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(any()) } throws NetworkException()

        // When & Then
        assertThrows<NetworkException> {
            authenticationRemoteDataSourceImpl.loginWithPassword(username, password)
        }
        coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
        coVerify(exactly = 1) { authenticationApiService.createSessionWithLogin(any()) }
        coVerify(exactly = 1) { authenticationApiService.createSession(any()) }
    }

    @Test
    fun `createSessionWithLogin should return sessionId when api call is successful`() = runTest {
        // Given
        val username = "testUser"
        val password = "testPassword"
        val requestToken = "unactive_request_token"
        val activeRequestToken = "active_request_token"
        val sessionId = "final_session_id"

        val unactiveTokenResponse = AuthenticationResponseDto(
            isSuccess = true,
            expiresAt = "2025-08-04",
            requestToken = requestToken
        )
        val activeTokenResponse = AuthenticationResponseDto(
            isSuccess = true,
            expiresAt = "2025-08-04",
            requestToken = activeRequestToken
        )
        val createSessionResponse = CreateSessionResponseDto(
            success = true,
            sessionId = sessionId
        )

        coEvery { authenticationApiService.createRequestToken() } returns unactiveTokenResponse
        coEvery { authenticationApiService.createSessionWithLogin(any()) } returns activeTokenResponse
        coEvery { authenticationApiService.createSession(any()) } returns createSessionResponse

        // When
        val result = authenticationRemoteDataSourceImpl.loginWithPassword(username, password)

        // Then
        assertThat(result).isEqualTo(createSessionResponse.sessionId)

        coVerify(exactly = 1) { authenticationApiService.createRequestToken() }
        coVerify(exactly = 1) { authenticationApiService.createSessionWithLogin(any()) }
        coVerify(exactly = 1) { authenticationApiService.createSession(any()) }
    }

    @Test
    fun `createSessionWithLogin should decode authentication response with status code`() = runTest {
        // Given
        val jsonString = """
            {
              "status_code": 200,
              "status_message": "Success",
              "success": true
            }
        """.trimIndent()

        // When
        val response = json.decodeFromString<AuthenticationResponseDto>(jsonString)

        // Then
        assertThat(response.statusCode).isNotNull()
        assertThat(response.statusCode).isEqualTo(200)
        assertThat(response.statusMessage).isEqualTo("Success")
    }

    @Test
    fun `decodeFromString should throw exception when status code is null`() {
        // Given
        val jsonString = """
        {
            "status_message": "Success",
            "success": true
        }
    """.trimIndent()

        // When & Then
        assertThrows<NullPointerException> {
            val response = json.decodeFromString<AuthenticationResponseDto>(jsonString)
            response.statusCode!!
        }
    }

}