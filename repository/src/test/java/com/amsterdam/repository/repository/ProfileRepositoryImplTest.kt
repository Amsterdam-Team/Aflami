package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.ProfileRepository
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.datasource.remote.ProfileRemoteDataSource
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto
import com.amsterdam.repository.mapper.local.toEntity
import com.amsterdam.repository.mapper.remote.toEntity
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class ProfileRepositoryImplTest {

    private lateinit var profileRepository: ProfileRepository
    private val profileRemoteDataSource: ProfileRemoteDataSource = mockk()
    private val profileLocalDataSource: ProfileLocalDataSource = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()

    @BeforeEach
    fun setUp() {
        profileRepository = ProfileRepositoryImpl(
            profileRemoteDataSource,
            profileLocalDataSource,
            authenticationRepository
        )
    }

    @Test
    fun `getAccountDetails should return local data if available and valid`() = runTest {
        // Arrange
        val fakeSessionId = "session123"
        val localResponse = AccountDetailsLocalDto(
            accountId = 12345,
            username = "testuser",
            avatarUrl = "/avatar.jpg"
        )
        val expectedEntity = localResponse.toEntity()

        coEvery { authenticationRepository.getSessionId() } returns fakeSessionId
        coEvery { profileLocalDataSource.getAccountDetails() } returns localResponse

        // Act
        val result = profileRepository.getAccountDetails()

        // Assert
        assertThat(result).isEqualTo(expectedEntity)
        coVerify(exactly = 1) { authenticationRepository.getSessionId() }
        coVerify(exactly = 1) { profileLocalDataSource.getAccountDetails() }
        coVerify(exactly = 0) { profileRemoteDataSource.getAccountDetails(any()) }
    }

    @Test
    fun `getAccountDetails should return remote data when local data is null`() = runTest {
        // Arrange
        val fakeSessionId = "session123"
        val remoteResponse = AccountDetailsRemoteDto(
            id = 12345,
            username = "testuser_remote",
            name = "Test User Remote",
            includeAdult = false,
            countryIsoCode = "US",
            languageCode = "en",
            accountAvatar = AccountDetailsRemoteDto.AccountAvatar(
                gravatar = AccountDetailsRemoteDto.Gravatar(
                    hash = "some_hash"
                ),
                movieDBData = AccountDetailsRemoteDto.MovieDBData(
                    avatarPath = "/avatar.jpg"
                )
            )
        )
        val expectedEntity = remoteResponse.toEntity()

        coEvery { authenticationRepository.getSessionId() } returns fakeSessionId
        coEvery { profileLocalDataSource.getAccountDetails() } returns null
        coEvery { profileRemoteDataSource.getAccountDetails(sessionId = fakeSessionId) } returns remoteResponse
        coEvery { profileLocalDataSource.upsertAccountDetails(any()) } returns Unit

        // Act
        val result = profileRepository.getAccountDetails()

        // Assert
        assertThat(result).isEqualTo(expectedEntity)
        coVerify(exactly = 1) { authenticationRepository.getSessionId() }
        coVerify(exactly = 1) { profileLocalDataSource.getAccountDetails() }
        coVerify(exactly = 1) { profileRemoteDataSource.getAccountDetails(sessionId = fakeSessionId) }
        coVerify(exactly = 1) { profileLocalDataSource.upsertAccountDetails(any()) }
    }

    @Test
    fun `getAccountDetails should return default entity when no sessionId is available`() =
        runTest {
            // Arrange
            coEvery { authenticationRepository.getSessionId() } returns ""

            // Act
            val result = profileRepository.getAccountDetails()

            // Assert
            assertThat(result.accountId).isEqualTo(0)
            assertThat(result.username).isEqualTo("")
            assertThat(result.avatarUrl).isEqualTo("")
            coVerify(exactly = 1) { authenticationRepository.getSessionId() }
            coVerify(exactly = 0) { profileLocalDataSource.getAccountDetails() }
            coVerify(exactly = 0) { profileRemoteDataSource.getAccountDetails(any()) }
        }
}