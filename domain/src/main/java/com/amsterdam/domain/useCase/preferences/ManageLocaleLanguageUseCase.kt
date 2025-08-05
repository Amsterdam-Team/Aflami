package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class ManageLocaleLanguageUseCase(
    private val preferencesRepository: AppPreferencesRepository,
) {
    suspend fun initAppLanguage(language: String) {
        preferencesRepository.getAppLanguage().firstOrNull()?.let {
            it.takeIf { it.isEmpty() }.let {
                setAppLanguage(Language.fromLanguage(language))
            }
            return
        } ?: setAppLanguage(Language.fromLanguage(language))
    }

    suspend fun setAppLanguage(language: Language) {
        preferencesRepository.setAppLanguage(language.value)
    }

    fun getAppLanguage(): Flow<Language> {
        return preferencesRepository.getAppLanguage().map {
            Language.fromLanguage(it)
        }
    }

    enum class Language(val value: String) {
        ENGLISH("en"),
        ARABIC("ar");

        companion object {
            fun fromLanguage(value: String): Language {
                return when (value) {
                    "en" -> ENGLISH
                    "ar" -> ARABIC
                    else -> ENGLISH
                }
            }
        }
    }
}