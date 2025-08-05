package com.amsterdam.viewmodel.profile

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.utils.RestrictionLevel

data class ProfileUiState(
    val isUserLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val userInfo: UserInfoUiState = UserInfoUiState(),
    val settingsState: SettingsMenuState = SettingsMenuState(),
    val profileErrorState: ProfileErrorState? = null
) {
    data class UserInfoUiState(
        val name: String = ""
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
