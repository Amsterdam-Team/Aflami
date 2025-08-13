package com.amsterdam.viewmodel.gameEnd

import com.amsterdam.viewmodel.shared.BaseViewModel

sealed interface ResultSideEffect : BaseViewModel.BaseUiEffect {
    data object NavigateToGame : ResultSideEffect
    data object NavigateBackToMenu : ResultSideEffect
}