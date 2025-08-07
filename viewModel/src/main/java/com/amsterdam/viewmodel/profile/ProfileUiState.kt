package com.amsterdam.viewmodel.profile

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language

data class ProfileUiState(
    val isUserLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val userInfo: UserInfoUiState = UserInfoUiState(),
    val settingsState: SettingsMenuState = SettingsMenuState(),
    val profileErrorState: ProfileErrorState? = null,
    val language: Language = Language.ENGLISH,
    val isDarkTheme: Boolean = false,
    val updatedLanguage: Language = Language.ENGLISH,
    val updatedIsDarkTheme: Boolean = false,
    val showLanguageDialog: Boolean = false,
    val showThemeDialog: Boolean = false,
    val appVersion: String = ""
) {
    data class UserInfoUiState(
        val username: String = "",
        val userAvatarUrl: String = "",
        val userPoints: Int = 0
    )

    data class SettingsMenuState(
        val isSettingsDialogVisible: Boolean = false,
        val isLogoutDialogVisible: Boolean = false,
        val isContentRestrictionDialogVisible: Boolean = false,
        val isLogoutButtonLoading: Boolean = false,
        val isContentRestrictionSaveButtonLoading: Boolean = false,
        val contentRestrictionLevel: RestrictionLevel = RestrictionLevel.STRICT
    )
}

sealed interface ProfileErrorState{
    data object UnknownError: ProfileErrorState

    companion object{
        fun toProfileErrorState(exception: AflamiException): ProfileErrorState{
            return when (exception){
                is AflamiException -> UnknownError
                else -> UnknownError
            }
        }
    }
}
