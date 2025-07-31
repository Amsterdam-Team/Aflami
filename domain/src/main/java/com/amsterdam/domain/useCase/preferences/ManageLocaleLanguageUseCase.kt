package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.flow.Flow

class ManageLocaleLanguageUseCase(
    private val preferencesRepository: AppPreferencesRepository,
) {
    suspend fun setDeviceLanguage(language: String) {
        preferencesRepository.setDeviceLanguage(language)
    }

    fun getDeviceLanguage(): Flow<String> {
        return preferencesRepository.getDeviceLanguage()
    }
}