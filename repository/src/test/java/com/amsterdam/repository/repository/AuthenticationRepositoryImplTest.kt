package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.repository.datasource.local.AuthenticationLocalSource
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteSource
import com.amsterdam.repository.mapper.local.SessionTypeMapper
import com.amsterdam.repository.security.CryptoData
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

    private val authenticationRemoteSource: AuthenticationRemoteSource = mockk()
    private val authenticationLocalSource: AuthenticationLocalSource = mockk()
    private val sessionTypeMapper: SessionTypeMapper = mockk()
    private val cryptoData: CryptoData = mockk()

    private val testUsername = "testUser"
    private val testPassword = "testPassword"
    private val testSessionId = "some_session_id_from_remote"
    private val encryptedSessionId = "encrypted_session_id"

    private val localLoggedInSessionTypeString = "LOGGED_IN"
    private val localNotLoggedInSessionTypeString = "NOT_LOGGED_IN"

    @BeforeEach
    fun setup() {
        clearAllMocks()
        repository = AuthenticationRepositoryImpl(
            authenticationRemoteSource = authenticationRemoteSource,
            authenticationLocalSource = authenticationLocalSource,
            sessionTypeMapper = sessionTypeMapper,
            cryptoData = cryptoData
        )
    }

    @Test
    fun `loginWithPassword should encrypt and cache session ID and set session type on success`() =
        runTest {
            coEvery {
                authenticationRemoteSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            } returns testSessionId
            every { cryptoData.encryptString(testSessionId) } returns encryptedSessionId
            coJustRun { authenticationLocalSource.cacheSessionId(encryptedSessionId) }
            every { sessionTypeMapper.toLocalSessionType(SessionType.LOGGED_IN) } returns localLoggedInSessionTypeString
            coJustRun { authenticationLocalSource.setSessionType(localLoggedInSessionTypeString) }

            repository.loginWithPassword(testUsername, testPassword)

            coVerify(exactly = 1) {
                authenticationRemoteSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            }
            verify(exactly = 1) { cryptoData.encryptString(testSessionId) }
            coVerify(exactly = 1) { authenticationLocalSource.cacheSessionId(encryptedSessionId) }
            verify(exactly = 1) { sessionTypeMapper.toLocalSessionType(SessionType.LOGGED_IN) }
            coVerify(exactly = 1) {
                authenticationLocalSource.setSessionType(
                    localLoggedInSessionTypeString
                )
            }
        }

    @Test
    fun `loginWithPassword should throw exception if remote login fails and not cache data`() =
        runTest {
            val expectedException = RuntimeException("Remote login failed")
            coEvery {
                authenticationRemoteSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            } throws expectedException

            val thrownException = assertThrows<RuntimeException> {
                repository.loginWithPassword(testUsername, testPassword)
            }

            assertThat(thrownException).isEqualTo(expectedException)
            coVerify(exactly = 1) {
                authenticationRemoteSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            }
            verify(exactly = 0) { cryptoData.encryptString(any()) }
            coVerify(exactly = 0) { authenticationLocalSource.cacheSessionId(any()) }
            verify(exactly = 0) { sessionTypeMapper.toLocalSessionType(any()) }
            coVerify(exactly = 0) { authenticationLocalSource.setSessionType(any()) }
        }

    @Test
    fun `getSessionId should return decrypted session ID when available`() = runTest {
        coEvery { authenticationLocalSource.getCachedSessionId() } returns encryptedSessionId
        every { cryptoData.decryptString(encryptedSessionId) } returns testSessionId

        val result = repository.getSessionId()

        assertThat(result).isEqualTo(testSessionId)
        coVerify(exactly = 1) { authenticationLocalSource.getCachedSessionId() }
        verify(exactly = 1) { cryptoData.decryptString(encryptedSessionId) }
    }

    @Test
    fun `getSessionId should throw UnknownException if decrypted result is null`() = runTest {
        val invalidCachedString = "" // Or "invalid_encrypted_data"

        coEvery { authenticationLocalSource.getCachedSessionId() } returns invalidCachedString
        every { cryptoData.decryptString(invalidCachedString) } returns null

        val thrownException = assertThrows<UnknownException> {
            repository.getSessionId()
        }

        assertThat(thrownException).isInstanceOf(UnknownException::class.java)
        // FIX IS HERE: The message is null because the UnknownException is thrown without a message.
        assertThat(thrownException.message).isNull()
        coVerify(exactly = 1) { authenticationLocalSource.getCachedSessionId() }
        verify(exactly = 1) { cryptoData.decryptString(invalidCachedString) }
    }


    @Test
    fun `getSessionId should throw RuntimeException if decryption fails`() = runTest {
        val decryptionException = RuntimeException("Decryption failed")
        coEvery { authenticationLocalSource.getCachedSessionId() } returns encryptedSessionId
        every { cryptoData.decryptString(encryptedSessionId) } throws decryptionException

        val thrownException = assertThrows<RuntimeException> {
            repository.getSessionId()
        }

        assertThat(thrownException).isEqualTo(decryptionException)
        coVerify(exactly = 1) { authenticationLocalSource.getCachedSessionId() }
        verify(exactly = 1) { cryptoData.decryptString(encryptedSessionId) }
    }

    @Test
    fun `setSessionType should map and set session type in local source`() = runTest {
        val sessionType = SessionType.LOGGED_IN
        every { sessionTypeMapper.toLocalSessionType(sessionType) } returns localLoggedInSessionTypeString
        coJustRun { authenticationLocalSource.setSessionType(localLoggedInSessionTypeString) }

        repository.setSessionType(sessionType)

        verify(exactly = 1) { sessionTypeMapper.toLocalSessionType(sessionType) }
        coVerify(exactly = 1) {
            authenticationLocalSource.setSessionType(
                localLoggedInSessionTypeString
            )
        }
    }

    @Test
    fun `getSessionType should get from local source and map to domain session type`() = runTest {
        val expectedSessionType = SessionType.LOGGED_IN
        coEvery { authenticationLocalSource.getSessionType() } returns localLoggedInSessionTypeString
        every { sessionTypeMapper.fromLocalSessionType(localLoggedInSessionTypeString) } returns expectedSessionType

        val result = repository.getSessionType()

        assertThat(result).isEqualTo(expectedSessionType)
        coVerify(exactly = 1) { authenticationLocalSource.getSessionType() }
        verify(exactly = 1) { sessionTypeMapper.fromLocalSessionType(localLoggedInSessionTypeString) }
    }

    @Test
    fun `logout should clear cached session ID and set session type to NOT_LOGGED_IN`() = runTest {
        coJustRun { authenticationLocalSource.clearCachedSessionId() }
        every { sessionTypeMapper.toLocalSessionType(SessionType.NOT_LOGGED_IN) } returns localNotLoggedInSessionTypeString
        coJustRun { authenticationLocalSource.setSessionType(localNotLoggedInSessionTypeString) }

        repository.logout()

        coVerify(exactly = 1) { authenticationLocalSource.clearCachedSessionId() }
        verify(exactly = 1) { sessionTypeMapper.toLocalSessionType(SessionType.NOT_LOGGED_IN) }
        coVerify(exactly = 1) {
            authenticationLocalSource.setSessionType(
                localNotLoggedInSessionTypeString
            )
        }
    }
}