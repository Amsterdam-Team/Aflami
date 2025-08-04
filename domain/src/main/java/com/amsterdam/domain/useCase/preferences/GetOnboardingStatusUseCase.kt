package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository

class GetOnboardingStatusUseCase(
    private val preferencesRepository: AppPreferencesRepository,
) {
    suspend operator fun invoke(): Boolean {
        return preferencesRepository.isOnboardingCompleted()
    }
}
