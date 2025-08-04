package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SetOnboardingCompletedUseCaseTest {

    private lateinit var setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase
    private val preferencesRepository: AppPreferencesRepository = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        setOnboardingCompletedUseCase = SetOnboardingCompletedUseCase(preferencesRepository)
    }

    @Test
    fun `setOnboardingCompleted should set onboarding completed status when isCompleted true`() =
        runTest {
            val isCompleted = true
            setOnboardingCompletedUseCase(isCompleted)
            coVerify(exactly = 1) {
                preferencesRepository.setOnboardingCompleted(isCompleted)
            }
        }
}