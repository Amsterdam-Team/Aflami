package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository

class ManageLocaleLanguageUseCase(
    private val preferencesRepository: AppPreferencesRepository,
) {
    suspend fun setCurrentLanguage(language: String) {
        preferencesRepository.setCurrentLanguage(language)
    }

    suspend fun getCurrentLanguage(): String {
        return preferencesRepository.getCurrentLanguage()
    }
}