package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository

class ManageLocaleLanguageUseCase(
    private val preferencesRepository: AppPreferencesRepository,
) {
    suspend fun setDeviceLanguage(language: String) {
        preferencesRepository.setDeviceLanguage(language)
    }
}