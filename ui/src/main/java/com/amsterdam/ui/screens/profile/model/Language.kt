package com.amsterdam.ui.screens.profile.model

import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.ui.R

enum class Language(
    val nameResourceId: Int,
    val exampleResourceId: Int
) {
    ENGLISH(
        R.string.english,
        R.string.language_sample_english
    ),
    ARABIC(
        R.string.arabic,
        R.string.language_sample_arabic
    );

    companion object {
        fun fromUiState(language: ManageLocaleLanguageUseCase.Language): Language {
            return when (language) {
                ManageLocaleLanguageUseCase.Language.ENGLISH -> ENGLISH
                ManageLocaleLanguageUseCase.Language.ARABIC -> ARABIC
            }
        }
    }
}