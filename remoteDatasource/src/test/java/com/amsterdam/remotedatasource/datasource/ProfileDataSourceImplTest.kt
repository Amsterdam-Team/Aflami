package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.ProfileApiService
import com.amsterdam.repository.dto.remote.profile.AccountDetailsDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class ProfileDataSourceImplTest {
    private lateinit var profileApiService: ProfileApiService
    private lateinit var profileDataSourceImpl: ProfileDataSourceImpl

    @BeforeEach
    fun setUp() {
        profileApiService = mockk()
        profileDataSourceImpl = ProfileDataSourceImpl(profileApiService)
    }

    @Test
    fun `getAccountDetails should return account details when api call is successful`() = runTest {
        //Given
        val sessionId = "session_id"
        val expectedAccountDetails = mockk<AccountDetailsDto>()
        coEvery { profileApiService.getAccountDetails(any()) } returns expectedAccountDetails

        //when
        val result = profileDataSourceImpl.getAccountDetails(sessionId)

        //then
        assertThat(result).isEqualTo(expectedAccountDetails)
        coVerify(exactly = 1) { profileApiService.getAccountDetails(sessionId) }
    }
    @Test
    fun `getAccountDetails should return null when api call fails`() = runTest {
        //Given
        val sessionId = "session_id"
        coEvery { profileApiService.getAccountDetails(any()) } throws Exception()
        //When & Then
        assertThrows <Exception>{ profileDataSourceImpl.getAccountDetails(sessionId) }
    }

}

