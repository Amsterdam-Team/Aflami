package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.ProfileRepository
import com.amsterdam.entity.AccountDetails
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.datasource.remote.ProfileRemoteDataSource
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto
import com.amsterdam.repository.mapper.local.toEntity
import com.amsterdam.repository.mapper.remote.toEntity
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ProfileRepositoryImplTest {

    private val profileRemoteDataSource: ProfileRemoteDataSource = mockk()
    private val profileLocalDataSource: ProfileLocalDataSource = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)

    private val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(
            profileRemoteDataSource,
            profileLocalDataSource,
            authenticationRepository
        )
    }

    @Test
    fun `getAccountDetails should return local data if available and valid`() = runTest {
        coEvery { authenticationRepository.getSessionId() } returns fakeSessionId
        coEvery { profileLocalDataSource.getAccountDetails() } returns fakeLocalResponse

        val result = profileRepository.getAccountDetails()

        assertThat(result).isEqualTo(fakeLocalResponse.toEntity())
    }

    @Test
    fun `getAccountDetails should Return Entity when Remote Response Is Available`() = runTest {
        coEvery { profileRemoteDataSource.getAccountDetails() } returns fakeRemoteResponse
        coEvery { authenticationRepository.getSessionId() } returns fakeSessionId
        coEvery { profileLocalDataSource.getAccountDetails() } returns null
        coEvery { profileLocalDataSource.upsertAccountDetails(any()) } returns Unit

        val result = profileRepository.getAccountDetails()

        assertThat(result).isEqualTo(fakeRemoteResponse.toEntity())
    }

    @Test
    fun `getAccountDetails should return default entity when no sessionId is available`() =
        runTest {
            coEvery { authenticationRepository.getSessionId() } returns ""

            val result = profileRepository.getAccountDetails()

            assertThat(result).isEqualTo(defaultAccountDetails)

        }

    private val fakeSessionId = "session123"

    private val fakeLocalResponse = AccountDetailsLocalDto(
        accountId = 12345,
        username = "testuser",
        avatarUrl = "/avatar.jpg"
    )

    private val fakeRemoteResponse = AccountDetailsRemoteDto(
        id = 12345,
        username = "testuser_remote",
        name = "Test User Remote",
        includeAdult = false,
        countryIsoCode = "US",
        languageCode = "en",
        accountAvatar = AccountDetailsRemoteDto.AccountAvatar(
            gravatar = AccountDetailsRemoteDto.Gravatar(hash = "some_hash"),
            movieDBData = AccountDetailsRemoteDto.MovieDBData(avatarPath = "/avatar.jpg")
        )
    )

    private val defaultAccountDetails = AccountDetails(
        accountId = 0,
        username = "",
        avatarUrl = ""
    )
}