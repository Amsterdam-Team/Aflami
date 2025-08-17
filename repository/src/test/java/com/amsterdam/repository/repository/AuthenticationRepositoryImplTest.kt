package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.repository.datasource.local.AuthenticationLocalDataSource
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteDataSource
import com.amsterdam.repository.mapper.toLocalDto
import com.amsterdam.repository.security.CryptoManager
import com.amsterdam.repository.utils.decryptString
import com.amsterdam.repository.utils.encryptString
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthenticationRepositoryImplTest {

    private lateinit var repository: AuthenticationRepository

    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource = mockk()
    private val authenticationLocalDataSource: AuthenticationLocalDataSource = mockk()
    private val profileLocalDataSource: ProfileLocalDataSource = mockk()
    private val cryptoManager: CryptoManager = mockk()

    private val testUsername = "testUser"
    private val testPassword = "testPassword"
    private val testSessionId = "some_session_id_from_remote"
    private val encryptedSessionId = "encrypted_session_id"

    @BeforeEach
    fun setup() {
        clearAllMocks()
        repository = AuthenticationRepositoryImpl(
            authenticationRemoteDataSource = authenticationRemoteDataSource,
            authenticationLocalDataSource = authenticationLocalDataSource,
            profileLocalDataSource = profileLocalDataSource,
            cryptoManager = cryptoManager
        )
    }

    @Test
    fun `loginWithPassword should encrypt and cache session ID and set session type on success`() =
        runTest {
            // Given
            coEvery {
                authenticationRemoteDataSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            } returns testSessionId
            every { cryptoManager.encryptString(testSessionId) } returns encryptedSessionId
            coJustRun { authenticationLocalDataSource.cacheSessionId(encryptedSessionId) }
            coJustRun { authenticationLocalDataSource.setSessionType(SessionType.LOGGED_IN.toLocalDto()) }

            // When
            repository.loginWithPassword(testUsername, testPassword)

            // Then
            coVerify(exactly = 1) {
                authenticationRemoteDataSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            }
            coVerify(exactly = 1) { authenticationLocalDataSource.cacheSessionId(encryptedSessionId) }
            coVerify(exactly = 1) { authenticationLocalDataSource.setSessionType(SessionType.LOGGED_IN.toLocalDto()) }
        }

    @Test
    fun `loginWithPassword should throw exception if remote login fails and not cache data`() =
        runTest {
            // Given
            val expectedException = RuntimeException("Remote login failed")
            coEvery {
                authenticationRemoteDataSource.loginWithPassword(
                    any(),
                    any()
                )
            } throws expectedException

            // When
            val thrownException = assertThrows<RuntimeException> {
                repository.loginWithPassword(testUsername, testPassword)
            }

            // Then
            assertThat(thrownException).isEqualTo(expectedException)
            coVerify(exactly = 1) {
                authenticationRemoteDataSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            }
            verify(exactly = 0) { cryptoManager.encryptString(any()) }
            coVerify(exactly = 0) { authenticationLocalDataSource.cacheSessionId(any()) }
            coVerify(exactly = 0) { authenticationLocalDataSource.setSessionType(any()) }
        }

    @Test
    fun `getSessionId should return decrypted session ID when available`() = runTest {
        // Given
        coEvery { authenticationLocalDataSource.getCachedSessionId() } returns encryptedSessionId
        every { cryptoManager.decryptString(encryptedSessionId) } returns testSessionId

        // When
        val result = repository.getSessionId()

        // Then
        assertThat(result).isEqualTo(testSessionId)
        coVerify(exactly = 1) { authenticationLocalDataSource.getCachedSessionId() }
        verify(exactly = 1) { cryptoManager.decryptString(encryptedSessionId) }
    }

    @Test
    fun `getSessionId should throw UnknownException if decrypted result is null`() = runTest {
        // Given
        coEvery { authenticationLocalDataSource.getCachedSessionId() } returns "invalid_data"
        every { cryptoManager.decryptString("invalid_data") } returns null

        // When / Then
        assertThrows<UnknownException> { repository.getSessionId() }

        coVerify(exactly = 1) { authenticationLocalDataSource.getCachedSessionId() }
        verify(exactly = 1) { cryptoManager.decryptString("invalid_data") }
    }

    @Test
    fun `setSessionType should store the correct string representation in local source`() =
        runTest {
            // Given
            val sessionType = SessionType.GUEST
            coJustRun { authenticationLocalDataSource.setSessionType(sessionType.toLocalDto()) }

            // When
            repository.setSessionType(sessionType)

            // Then
            coVerify(exactly = 1) { authenticationLocalDataSource.setSessionType(sessionType.toLocalDto()) }
        }

    @Test
    fun `getSessionType should return the correct domain session type from local source`() =
        runTest {
            // Given
            coEvery { authenticationLocalDataSource.getSessionType() } returns "GUEST"

            // When
            val result = repository.getSessionType()

            // Then
            assertThat(result).isEqualTo(SessionType.GUEST)
            coVerify(exactly = 1) { authenticationLocalDataSource.getSessionType() }
        }

    @Test
    fun `logout should clear local data and delete account details`() = runTest {
        // Given
        coJustRun { authenticationLocalDataSource.clearCachedSessionId() }
        coJustRun { authenticationLocalDataSource.setSessionType(SessionType.NOT_LOGGED_IN.toLocalDto()) }
        coJustRun { profileLocalDataSource.deleteAccountDetails() }

        // When
        repository.logout()

        // Then
        coVerify(exactly = 1) { authenticationLocalDataSource.clearCachedSessionId() }
        coVerify(exactly = 1) { authenticationLocalDataSource.setSessionType(SessionType.NOT_LOGGED_IN.toLocalDto()) }
        coVerify(exactly = 1) { profileLocalDataSource.deleteAccountDetails() }
    }
}