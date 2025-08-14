package com.amsterdam.domain.useCase.profile

import com.amsterdam.domain.repository.ProfileRepository
import com.amsterdam.entity.AccountDetails
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetAccountDetailsUseCaseTest {
    private val profileRepository: ProfileRepository = mockk()
    private val getAccountDetailsUseCase by lazy {
        GetAccountDetailsUseCase(profileRepository)
    }

    @Test
    fun `should return account details`() = runTest {

        coEvery { profileRepository.getAccountDetails() } returns expectedAccountDetails

        val accountDetails = getAccountDetailsUseCase()

        assertThat(accountDetails).isEqualTo(expectedAccountDetails)
    }

    private val expectedAccountDetails = AccountDetails(
        accountId = 2,
        username = "test_user",
        avatarUrl = "test_avatar.jpg"
    )
}