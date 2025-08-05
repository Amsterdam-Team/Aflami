package com.amsterdam.viewmodel.profile

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.authentication.LogoutUseCase
import com.amsterdam.domain.useCase.preferences.ManageAppThemeUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageRestrictionLevelUseCase
import com.amsterdam.domain.useCase.profile.GetAccountDetailsUseCase
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.entity.AccountDetails
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getSessionTypeUseCase: GetsSessionType,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    private val manageAppThemeUseCase: ManageAppThemeUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val manageRestrictionLevelUseCase: ManageRestrictionLevelUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ProfileUiState, ProfileEffect>(
    initialState = ProfileUiState(),
    dispatcherProvider = dispatcherProvider
), ProfileInteractionListener {
    init {
        loadProfileData()
        loadSettings()
        loadRestrictionLevel()
    }

    private fun loadRestrictionLevel() {
        viewModelScope.launch {
            manageRestrictionLevelUseCase.getRestrictionLevel().collect { restrictionLevel ->
                updateState {
                    it.copy(
                        settingsState = it.settingsState.copy(
                            contentRestrictionLevel = restrictionLevel
                        )
                    )
                }
            }
        }
    }

    private fun loadProfileData() {
        updateState { state -> state.copy(isLoading = true) }
        tryToExecute(
            action = { getSessionTypeUseCase() },
            onSuccess = ::onGetSessionTypeSuccess,
            onError = ::onUserDataNotLoaded
        )
    }

    private fun loadSettings() {
        viewModelScope.launch {
            manageLocaleLanguageUseCase.getAppLanguage().collect { language ->
                onGetAppLanguage(language)
            }
        }
        viewModelScope.launch {
            manageAppThemeUseCase.getAppTheme().collect { isDarkTheme ->
                onGetAppTheme(isDarkTheme)
            }
        }
    }

    private fun onGetAppTheme(isDarkTheme: Boolean) {
        updateState { state ->
            state.copy(
                isDarkTheme = isDarkTheme,
                updatedIsDarkTheme = isDarkTheme
            )
        }
    }

    private fun onGetAppLanguage(language: ManageLocaleLanguageUseCase.Language) {
        updateState { state ->
            state.copy(
                language = language,
                updatedLanguage = language
            )
        }
    }

    private fun onGetSessionTypeSuccess(sessionType: SessionType) {
        if (sessionType != SessionType.LOGGED_IN) {
            updateState { state -> state.copy(isUserLoggedIn = false, isLoading = false) }
            return
        }
        getUserProfileInfo()
    }

    private fun getUserProfileInfo() {
        updateState { state -> state.copy(isUserLoggedIn = true, isLoading = false) }
        tryToExecute(
            action = { getAccountDetailsUseCase() },
            onSuccess = ::onGetUserProfileInfoSuccess,
            onError = ::onUserDataNotLoaded
        )
    }

    private fun onError(aflamiException: AflamiException) {
        updateState {
            it.copy(
                profileErrorState = ProfileErrorState.toProfileErrorState(aflamiException)
            )
        }
        sendNewEffect(ProfileEffect.ShowError)
    }

    private fun onGetUserProfileInfoSuccess(accountDetails: AccountDetails) {
        updateState { state ->
            state.copy(
                userInfo = state.userInfo.copy(
                    username = accountDetails.username,
                    userAvatarUrl = accountDetails.avatarUrl
                )
            )
        }
    }

    override fun onClickLogin() {
        sendNewNavigationEffect(ProfileEffect.NavigateToLogin)
    }

    override fun onClickLanguageSetting() {
        updateState { state -> state.copy(showLanguageDialog = true) }
    }

    override fun onChangeLanguage(language: ManageLocaleLanguageUseCase.Language) {
        updateState { state -> state.copy(language = language) }
    }

    override fun onApplyLanguage() {
        tryToExecute(
            action = { manageLocaleLanguageUseCase.setAppLanguage(state.value.language) },
            onSuccess = ::onApplyLanguageSuccess,
            onError = ::onApplyLanguageFailure
        )
    }

    private fun onApplyLanguageFailure(aflamiException: AflamiException) {
        updateState { state -> state.copy(language = state.updatedLanguage) }
        sendNewEffect(ProfileEffect.LanguageNotChanged)
    }

    private fun onApplyLanguageSuccess(unit: Unit) {
        updateState { state -> state.copy(updatedLanguage = state.language) }
        onDismissLanguageDialog()
        sendNewEffect(ProfileEffect.LanguageChanged)
    }

    override fun onDismissLanguageDialog() {
        updateState { state -> state.copy(showLanguageDialog = false) }
    }

    override fun onClickThemeSetting() {
        updateState { state -> state.copy(showThemeDialog = true) }
    }

    override fun onChangeTheme(isDarkTheme: Boolean) {
        updateState { state -> state.copy(isDarkTheme = isDarkTheme) }
    }

    override fun onApplyTheme() {
        tryToExecute(
            action = { manageAppThemeUseCase.setAppTheme(state.value.isDarkTheme) },
            onSuccess = ::onApplyThemeSuccess,
            onError = ::onApplyThemeFailure
        )
        onApplyThemeSuccess(Unit)
    }

    private fun onApplyThemeFailure(aflamiException: AflamiException) {
        updateState { state -> state.copy(isDarkTheme = state.updatedIsDarkTheme) }
        sendNewEffect(ProfileEffect.ThemeNotChanged)
    }

    private fun onApplyThemeSuccess(unit: Unit) {
        updateState { state -> state.copy(updatedIsDarkTheme = state.isDarkTheme) }
        onDismissThemeDialog()
        sendNewEffect(ProfileEffect.ThemeChanged)
    }

    override fun onDismissThemeDialog() {
        updateState { state -> state.copy(showThemeDialog = false) }
    }

    private fun onUserDataNotLoaded(aflamiException: AflamiException) {
        when (aflamiException) {
            is NetworkException -> sendNewEffect(ProfileEffect.UserDataNotLoaded)
            else -> {}
        }
    }

    //region Settings
    override fun onClickSettings() {
        updateState {
            it.copy(
                settingsState = it.settingsState.copy(
                    isSettingsDialogVisible = true,
                )
            )
        }
    }

    override fun onDismissSettingsDialog() {
        updateState {
            it.copy(
                settingsState = it.settingsState.copy(
                    isSettingsDialogVisible = false
                )
            )
        }
    }

    override fun onDismissLogoutDialog() {
        updateState {
            it.copy(
                settingsState = it.settingsState.copy(
                    isLogoutDialogVisible = false
                )
            )
        }
    }

    override fun onDismissContentRestrictionDialog() {
        updateState {
            it.copy(
                settingsState = it.settingsState.copy(
                    isContentRestrictionDialogVisible = false
                )
            )
        }
    }

    override fun onClickLogout() {
        updateState {
            it.copy(
                settingsState = it.settingsState.copy(
                    isLogoutDialogVisible = true
                )
            )
        }
    }

    override fun onClickConfirmLogout() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    settingsState = it.settingsState.copy(
                        isLogoutButtonLoading = true
                    )
                )
            }
        }
        viewModelScope.launch {
            tryToExecute(
                action = ::onConfirmLogout,
                onSuccess = { onConfirmLogoutSuccess() },
                onError = ::onError,
                onCompletion = ::onConfirmLogoutCompletion
            )
        }
    }

    private suspend fun onConfirmLogout() {
        logoutUseCase()
    }

    private fun onConfirmLogoutSuccess() {
        sendNewNavigationEffect(ProfileEffect.NavigateToLogin)
    }

    private fun onConfirmLogoutCompletion() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    settingsState = it.settingsState.copy(
                        isLogoutButtonLoading = false,
                        isLogoutDialogVisible = false,
                        isSettingsDialogVisible = false
                    )
                )
            }
        }
    }

    override fun onClickForgotPassword() {
        sendNewNavigationEffect(ProfileEffect.NavigateToResetPassword)
    }

    override fun onClickContentRestriction() {
        updateState {
            it.copy(
                settingsState = it.settingsState.copy(
                    isContentRestrictionDialogVisible = true
                )
            )
        }
    }

    override fun onUpdateRestrictionLevel(restrictionLevel: RestrictionLevel) {
        updateState {
            it.copy(
                settingsState = it.settingsState.copy(
                    contentRestrictionLevel = restrictionLevel
                )
            )
        }
    }

    override fun onSaveRestrictionLevel() {
        updateState {
            it.copy(
                settingsState = it.settingsState.copy(
                    isContentRestrictionSaveButtonLoading = true
                )
            )
        }

        tryToExecute(
            action = { saveRestrictionLevel() },
            onSuccess = { _ -> },
            onError = ::onError,
            onCompletion = ::onSaveRestrictionLevelCompletion
        )
    }

    override fun onClickRating() {
        sendNewNavigationEffect(ProfileEffect.NavigateToMyRating)
    }

    private suspend fun saveRestrictionLevel() {
        manageRestrictionLevelUseCase.setRestrictionLevel(
            state.value.settingsState.contentRestrictionLevel
        )
    }

    private fun onSaveRestrictionLevelCompletion() {
        updateState {
            it.copy(
                settingsState = it.settingsState.copy(
                    isContentRestrictionSaveButtonLoading = false,
                    isContentRestrictionDialogVisible = false,
                    isSettingsDialogVisible = false
                )
            )
        }
    }

    //endregion
}