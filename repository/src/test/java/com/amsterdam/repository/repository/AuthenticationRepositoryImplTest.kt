import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.repository.datasource.local.AuthenticationLocalSource
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteSource
import com.amsterdam.repository.mapper.local.toLocalDto
import com.amsterdam.repository.repository.AuthenticationRepositoryImpl
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
    private val cryptoData: CryptoData = mockk()

    private val testUsername = "testUser"
    private val testPassword = "testPassword"
    private val testSessionId = "some_session_id_from_remote"
    private val encryptedSessionId = "encrypted_session_id"
    private val localLoggedInSessionTypeString = "LOGGED_IN"

    @BeforeEach
    fun setup() {
        clearAllMocks()
        repository = AuthenticationRepositoryImpl(
            authenticationRemoteSource = authenticationRemoteSource,
            authenticationLocalSource = authenticationLocalSource,
            cryptoData = cryptoData
        )
    }

    @Test
    fun `loginWithPassword should encrypt and cache session ID and set session type on success`() =
        runTest {
            // Arrange
            coEvery {
                authenticationRemoteSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            } returns testSessionId
            every { cryptoData.encryptString(testSessionId) } returns encryptedSessionId
            coJustRun { authenticationLocalSource.cacheSessionId(encryptedSessionId) }
            coJustRun { authenticationLocalSource.setSessionType(SessionType.LOGGED_IN.toLocalDto()) }

            // Act
            repository.loginWithPassword(testUsername, testPassword)

            // Assert
            coVerify(exactly = 1) {
                authenticationRemoteSource.loginWithPassword(
                    testUsername,
                    testPassword
                )
            }
            verify(exactly = 1) { cryptoData.encryptString(testSessionId) }
            coVerify(exactly = 1) { authenticationLocalSource.cacheSessionId(encryptedSessionId) }
            coVerify(exactly = 1) { authenticationLocalSource.setSessionType(SessionType.LOGGED_IN.toLocalDto()) }
        }

    @Test
    fun `loginWithPassword should throw exception if remote login fails and not cache data`() =
        runTest {
            // Arrange
            val expectedException = RuntimeException("Remote login failed")
            coEvery {
                authenticationRemoteSource.loginWithPassword(
                    any(),
                    any()
                )
            } throws expectedException

            // Act & Assert
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
            coVerify(exactly = 0) { authenticationLocalSource.setSessionType(any()) }
        }


    @Test
    fun `getSessionId should return decrypted session ID when available`() = runTest {
        // Arrange
        coEvery { authenticationLocalSource.getCachedSessionId() } returns encryptedSessionId
        every { cryptoData.decryptString(encryptedSessionId) } returns testSessionId

        // Act
        val result = repository.getSessionId()

        // Assert
        assertThat(result).isEqualTo(testSessionId)
        coVerify(exactly = 1) { authenticationLocalSource.getCachedSessionId() }
        verify(exactly = 1) { cryptoData.decryptString(encryptedSessionId) }
    }

    @Test
    fun `getSessionId should throw UnknownException if decrypted result is null`() = runTest {
        // Arrange
        coEvery { authenticationLocalSource.getCachedSessionId() } returns "invalid_data"
        every { cryptoData.decryptString("invalid_data") } returns null

        // Act & Assert
        assertThrows<UnknownException> {
            repository.getSessionId()
        }

        coVerify(exactly = 1) { authenticationLocalSource.getCachedSessionId() }
        verify(exactly = 1) { cryptoData.decryptString("invalid_data") }
    }

    @Test
    fun `setSessionType should store the correct string representation in local source`() =
        runTest {
            // Arrange
            val sessionType = SessionType.GUEST
            coJustRun { authenticationLocalSource.setSessionType(sessionType.toLocalDto()) }

            // Act
            repository.setSessionType(sessionType)

            // Assert
            coVerify(exactly = 1) { authenticationLocalSource.setSessionType(sessionType.toLocalDto()) }
        }

    @Test
    fun `getSessionType should return the correct domain session type from local source`() =
        runTest {
            // Arrange
            coEvery { authenticationLocalSource.getSessionType() } returns "GUEST"

            // Act
            val result = repository.getSessionType()

            // Assert
            assertThat(result).isEqualTo(SessionType.GUEST)
            coVerify(exactly = 1) { authenticationLocalSource.getSessionType() }
        }

    @Test
    fun `logout should delete remote session and then clear local data on success`() = runTest {
        // Arrange
        coEvery { authenticationLocalSource.getCachedSessionId() } returns encryptedSessionId
        every { cryptoData.decryptString(encryptedSessionId) } returns testSessionId
        coEvery { authenticationRemoteSource.deleteSession(testSessionId) } returns true
        coJustRun { authenticationLocalSource.clearCachedSessionId() }
        coJustRun { authenticationLocalSource.setSessionType(SessionType.NOT_LOGGED_IN.toLocalDto()) }

        // Act
        repository.logout()

        // Assert
        coVerify(exactly = 1) { authenticationRemoteSource.deleteSession(testSessionId) }
        coVerify(exactly = 1) { authenticationLocalSource.clearCachedSessionId() }
        coVerify(exactly = 1) { authenticationLocalSource.setSessionType(SessionType.NOT_LOGGED_IN.toLocalDto()) }
    }

    @Test
    fun `logout should not clear local data if remote session deletion fails`() = runTest {
        // Arrange
        coEvery { authenticationLocalSource.getCachedSessionId() } returns encryptedSessionId
        every { cryptoData.decryptString(encryptedSessionId) } returns testSessionId
        coEvery { authenticationRemoteSource.deleteSession(testSessionId) } returns false

        // Act
        repository.logout()

        // Assert
        coVerify(exactly = 1) { authenticationRemoteSource.deleteSession(testSessionId) }
        coVerify(exactly = 0) { authenticationLocalSource.clearCachedSessionId() }
        coVerify(exactly = 0) { authenticationLocalSource.setSessionType(any()) }
    }
}