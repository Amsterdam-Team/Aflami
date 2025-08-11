package com.amsterdam.viewmodel.gameEnd

import androidx.lifecycle.SavedStateHandle
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ResultUiState, ResultSideEffect>(
    initialState = ResultUiState(),
    dispatcherProvider = dispatcherProvider
), ResultInteractionListener {

    init {
        val points = savedStateHandle.get<Int>("points") ?: 0
        val time = savedStateHandle.get<Int>("time") ?: 0

        updateState {
            it.copy(points = points, timeInSeconds = time)
        }
    }

    override fun onClickPlayAgain() {
        sendNewNavigationEffect(ResultSideEffect.NavigateToGame)
    }

    override fun onClickBackToMenu() {
        sendNewNavigationEffect(ResultSideEffect.NavigateBackToMenu)
    }

    override fun onClickClose() {
        onClickBackToMenu()
    }
}