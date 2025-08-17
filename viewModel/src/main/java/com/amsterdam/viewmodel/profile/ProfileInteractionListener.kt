package com.amsterdam.viewmodel.profile

import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase

import com.amsterdam.domain.utils.RestrictionLevel

interface ProfileInteractionListener {
    fun onClickLogin()

    fun onClickSettings()
    fun onDismissSettingsDialog()

    fun onClickLogout()
    fun onDismissLogoutDialog()

    fun onClickForgotPassword()

    fun onClickContentRestriction()
    fun onDismissContentRestrictionDialog()

    fun onClickConfirmLogout()

    fun onUpdateRestrictionLevel(restrictionLevel: RestrictionLevel)
    fun onSaveRestrictionLevel()

    fun onClickLanguageSetting()
    fun onChangeLanguage(language: ManageLocaleLanguageUseCase.Language)
    fun onApplyLanguage()
    fun onDismissLanguageDialog()

    fun onClickThemeSetting()
    fun onChangeTheme(isDarkTheme: Boolean)
    fun onApplyTheme()
    fun onDismissThemeDialog()

    fun onClickRating()
}