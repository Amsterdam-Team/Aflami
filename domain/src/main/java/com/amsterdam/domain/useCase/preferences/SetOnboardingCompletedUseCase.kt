package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository


class SetOnboardingCompletedUseCase(
    private val preferencesRepository: AppPreferencesRepository
) {
    suspend operator fun invoke(isCompleted: Boolean) {
        preferencesRepository.setOnboardingCompleted(isCompleted)
    }
}