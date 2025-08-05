package com.amsterdam.viewmodel.profile

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.authentication.LogoutUseCase
import com.amsterdam.domain.useCase.preferences.ManageRestrictionLevelUseCase
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getSessionTypeUseCase: GetsSessionType,
    private val logoutUseCase: LogoutUseCase,
    private val manageRestrictionLevelUseCase: ManageRestrictionLevelUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ProfileUiState, ProfileEffect>(
    initialState = ProfileUiState(),
    dispatcherProvider = dispatcherProvider
), ProfileInteractionListener {
    init {
        loadProfileData()
        loadRestrictionLevel()
    }

    private fun loadRestrictionLevel() {
        viewModelScope.launch {
            manageRestrictionLevelUseCase.getRestrictionLevel().collect{ restrictionLevel ->
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
            onError = ::onError
        )
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
    }

    private fun onError(aflamiException: AflamiException) {
        updateState {
            it.copy(
                profileErrorState = ProfileErrorState.toProfileErrorState(aflamiException)
            )
        }
        sendNewEffect(ProfileEffect.ShowError)
    }

    override fun onClickLogin() {
        sendNewNavigationEffect(ProfileEffect.NavigateToLogin)
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

    private suspend fun onConfirmLogout(){
        logoutUseCase()
    }

    private fun onConfirmLogoutSuccess(){
        sendNewNavigationEffect(ProfileEffect.NavigateToLogin)
    }

    private fun onConfirmLogoutCompletion(){
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

    private suspend fun saveRestrictionLevel(){
        manageRestrictionLevelUseCase.setRestrictionLevel(
            state.value.settingsState.contentRestrictionLevel
        )
    }

    private fun onSaveRestrictionLevelCompletion(){
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