package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.ProfileApiService
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProfileRemoteDataSourceImplTest {

    private val profileApiService: ProfileApiService = mockk()
    private val profileDataSourceImpl: ProfileRemoteDataSourceImpl =
        ProfileRemoteDataSourceImpl(profileApiService)

    @Test
    fun `getAccountDetails should return account details on successful API call`() = runTest {
        coEvery { profileApiService.getAccountDetails() } returns expectedAccountDetails

        val result = profileDataSourceImpl.getAccountDetails()

        assertThat(result).isEqualTo(expectedAccountDetails)
    }

    @Test
    fun `getAccountDetails should call getAccountDetails exactly once on a successful API call`() =
        runTest {
            coEvery { profileApiService.getAccountDetails() } returns expectedAccountDetails

            profileDataSourceImpl.getAccountDetails()

            coVerify(exactly = 1) { profileApiService.getAccountDetails() }
        }

    @Test
    fun `getAccountDetails should throw NetworkException when the API call fails`() = runTest {
        coEvery { profileApiService.getAccountDetails() } throws networkException

        assertThrows<NetworkException> { profileDataSourceImpl.getAccountDetails() }
    }

    private val expectedAccountDetails = mockk<AccountDetailsRemoteDto>()
    private val networkException = NetworkException()

}