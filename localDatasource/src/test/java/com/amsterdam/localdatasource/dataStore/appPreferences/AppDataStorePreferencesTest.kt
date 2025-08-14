package com.amsterdam.localdatasource.dataStore.appPreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.amsterdam.localdatasource.dataStore.AuthenticationLocalDataSourceImpl
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class AuthenticationLocalDataSourceImplTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var file: File
    private lateinit var authenticationLocalDataSourceImpl: AuthenticationLocalDataSourceImpl
    @BeforeEach
    fun setup() {
        file = File.createTempFile("test", ".preferences_pb").apply {
            deleteOnExit()
        }
        dataStore = PreferenceDataStoreFactory.create { file }
        authenticationLocalDataSourceImpl = AuthenticationLocalDataSourceImpl(dataStore)
    }

    @AfterEach
    fun cleanup() {
        file.delete()
    }

    @Test
    fun `getSessionType should return empty string when no value is set`() = runTest {
        //When
        val result = authenticationLocalDataSourceImpl.getSessionType()
        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `setSessionType should save provided value`() = runTest {
        //Given
        val userType = "guest"
        //When
        authenticationLocalDataSourceImpl.setSessionType(userType)
        //Then
        val result = authenticationLocalDataSourceImpl.getSessionType()
        assertThat(result).isEqualTo(userType)
    }
}