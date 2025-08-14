package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetOnboardingStatusUseCaseTest {

    private val preferencesRepository: AppPreferencesRepository = mockk(relaxed = true)
    private val getOnboardingStatusUseCase by lazy {
        GetOnboardingStatusUseCase(preferencesRepository)
    }

    @Test
    fun `should return true when onboarding is completed`() = runTest {
        coEvery { preferencesRepository.isOnboardingCompleted() } returns true
        getOnboardingStatusUseCase()
        coVerify(exactly = 1) { preferencesRepository.isOnboardingCompleted() }
    }
}