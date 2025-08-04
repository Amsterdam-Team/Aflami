package com.amsterdam.viewmodel.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.useCase.preferences.SetOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase
) : ViewModel(), OnboardingInteractionListener {

    private val _uiState = MutableStateFlow(
        OnboardingUiState(totalPages = 4)
    )
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<OnboardingEffect>()
    val effect = _effect.receiveAsFlow()

    override fun onNextPageClicked() {
        if (_uiState.value.currentPageIndex < _uiState.value.totalPages - 1) {
            val newIndex = _uiState.value.currentPageIndex + 1
            _uiState.value = _uiState.value.copy(
                currentPageIndex = newIndex,
                isLastPage = newIndex == _uiState.value.totalPages - 1
            )
        }
    }

    override fun onPreviousPageClicked() {
        if (_uiState.value.currentPageIndex > 0) {
            val newIndex = _uiState.value.currentPageIndex - 1
            _uiState.value = _uiState.value.copy(
                currentPageIndex = newIndex,
                isLastPage = newIndex == _uiState.value.totalPages - 1
            )
        }
    }

    override fun onSkipClicked() {
        viewModelScope.launch {
            setOnboardingCompletedUseCase(true)
            _effect.send(OnboardingEffect.NavigateToLoginScreen)
        }
    }

    override fun onGetStartedClicked() {
        viewModelScope.launch {
            setOnboardingCompletedUseCase(true)
            _effect.send(OnboardingEffect.NavigateToLoginScreen)
        }
    }

    fun setCurrentPage(newIndex: Int) {
        _uiState.value = _uiState.value.copy(
            currentPageIndex = newIndex,
            isLastPage = newIndex == _uiState.value.totalPages - 1
        )
    }
}

