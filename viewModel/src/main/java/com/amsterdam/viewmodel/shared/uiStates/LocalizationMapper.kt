package com.amsterdam.viewmodel.shared.uiStates

import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language
import java.util.Locale

fun String.toLocalizedName(language: String = Locale.getDefault().language): String {
    return if (language == Language.ARABIC.value) {
        val locale = Locale("", this)
        locale.getDisplayCountry(Locale(Language.ARABIC.value))
    } else {
        this.uppercase(Locale.ENGLISH)
    }
}