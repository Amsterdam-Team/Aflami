package com.amsterdam.domain.useCase.profile

import com.amsterdam.domain.repository.ProfileRepository
import com.amsterdam.entity.AccountDetails
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAccountDetailsUseCaseTest {
    private lateinit var profileRepository: ProfileRepository
    private lateinit var getAccountDetailsUseCase: GetAccountDetailsUseCase

    @BeforeEach
    fun setUp() {
        profileRepository = mockk()
        getAccountDetailsUseCase = GetAccountDetailsUseCase(profileRepository)
    }

    @Test
    fun `should return account details`() = runTest {
        //Given
        val exceptedAccountDetails = AccountDetails(
            accountId = 2,
            username = "test_user",
            avatarUrl = "test_avatar.jpg"
        )
        coEvery { profileRepository.getAccountDetails() } returns exceptedAccountDetails
        //When
        val accountDetails = getAccountDetailsUseCase()
        //Then
        assertThat(accountDetails).isEqualTo(exceptedAccountDetails)
        coVerify(exactly = 1) { profileRepository.getAccountDetails() }
    }
}