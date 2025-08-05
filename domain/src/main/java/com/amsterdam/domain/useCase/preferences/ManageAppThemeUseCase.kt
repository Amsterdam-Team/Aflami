package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.flow.Flow

class ManageAppThemeUseCase(
    private val preferencesRepository: AppPreferencesRepository,
) {
    suspend fun setAppTheme(isDarkTheme: Boolean) {
        preferencesRepository.setAppTheme(isDarkTheme)
    }

    fun getAppTheme(): Flow<Boolean> {
        return preferencesRepository.getAppTheme()
    }
}