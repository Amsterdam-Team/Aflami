package com.amsterdam.localdatasource.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.amsterdam.domain.utils.SessionType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class AuthenticationLocalDataSourceImplTest {
    private val file: File by lazy {
        File.createTempFile("test", ".preferences_pb").apply { deleteOnExit() }
    }
    private val dataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create { file }
    }
    private val authenticationLocalDataSourceImpl by lazy {
        AuthenticationLocalDataSourceImpl(dataStore)
    }

    @BeforeEach
    fun setup() {
        file.writeText("")
    }

    @Test
    fun `getSessionType should return empty string when no value`() = runTest {
        val result = authenticationLocalDataSourceImpl.getSessionType()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getSessionType should return session type string when has value`() = runTest {
        authenticationLocalDataSourceImpl.setSessionType(userType)

        val result = authenticationLocalDataSourceImpl.getSessionType()

        assertThat(result).isEqualTo(userType)
    }

    @Test
    fun `setSessionType should save provided value when called`() = runTest {
        authenticationLocalDataSourceImpl.setSessionType(userType)
        val result = authenticationLocalDataSourceImpl.getSessionType()

        assertThat(result).isEqualTo(userType)
    }

    @Test
    fun `getCachedSessionId should return empty string when no value`() = runTest {
        val result = authenticationLocalDataSourceImpl.getCachedSessionId()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getCachedSessionId should return cached session id when has value`() = runTest {
        authenticationLocalDataSourceImpl.cacheSessionId(sessionId)

        val result = authenticationLocalDataSourceImpl.getCachedSessionId()

        assertThat(result).isEqualTo(sessionId)
    }

    @Test
    fun `cacheSessionId should save provided value when called`() = runTest {
        authenticationLocalDataSourceImpl.cacheSessionId(sessionId)
        val result = authenticationLocalDataSourceImpl.getCachedSessionId()

        assertThat(result).isEqualTo(sessionId)
    }

    @Test
    fun `clearCachedSessionId should clear session id when called`() = runTest {
        authenticationLocalDataSourceImpl.cacheSessionId(sessionId)

        authenticationLocalDataSourceImpl.clearCachedSessionId()
        val result = authenticationLocalDataSourceImpl.getCachedSessionId()

        assertThat(result).isEmpty()
    }
}

private val userType = SessionType.GUEST.name
private const val sessionId = "sessionId"