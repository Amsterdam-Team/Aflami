package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.ProfileRepository
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.datasource.remote.ProfileRemoteDataSource
import com.amsterdam.repository.mapper.remote.toEntity
import com.amsterdam.repository.utils.accountDetails
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class ProfileRepositoryImplTest {
    private lateinit var profileDataSource: ProfileRemoteDataSource
    private lateinit var profileLocalDataSource: ProfileLocalDataSource
    private lateinit var profileRepository: ProfileRepository
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setUp() {
        profileDataSource = mockk()
        authenticationRepository = mockk()
        profileRepository = ProfileRepositoryImpl(profileDataSource, profileLocalDataSource,authenticationRepository)
    }

    @Test
    fun `getAccountDetails should return correct AccountDetails from dataSource`() = runTest {
        // Given
        val fakeSessionId = "session123"
        val remoteResponse = accountDetails
        val expectedEntity = accountDetails

        coEvery { authenticationRepository.getSessionId() } returns fakeSessionId
        coEvery { profileDataSource.getAccountDetails(sessionId = fakeSessionId) } returns remoteResponse

        // When
        val result = profileRepository.getAccountDetails()

        // Then
        assertThat(result).isEqualTo(expectedEntity.toEntity())

        coVerify { authenticationRepository.getSessionId() }
        coVerify { profileDataSource.getAccountDetails(sessionId = fakeSessionId) }
    }


}