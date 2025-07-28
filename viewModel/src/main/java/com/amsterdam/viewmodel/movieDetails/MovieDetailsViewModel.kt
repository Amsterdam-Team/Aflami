package com.amsterdam.viewmodel.movieDetails

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.MovieAndSeriesDetailsDialogType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    args: MovieDetailsArgs,
    private val movieDetailsUiStateMapper: MovieDetailsUiStateMapper,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getsSessionType: GetsSessionType,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(
    MovieDetailsUiState(),
    dispatcherProvider
), MovieDetailsInteractionListener {

    init {
        val movieId = args.movieId!!
        updateState { it.copy(movieId = movieId) }
        loadMovieDetails()
    }

    private fun loadMovieDetails() {
        updateState { it.copy(isLoading = true, networkError = false) }
        tryToExecute(
            action = ::getMovieDetails,
            onSuccess =::onGetMovieDetailsSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private suspend fun getMovieDetails() =
        getMovieDetailsUseCase(state.value.movieId)

    private fun onGetMovieDetailsSuccess(movieDetails: GetMovieDetailsUseCase.MovieDetails) =
        updateState { movieDetailsUiStateMapper.toUiState(movieDetails) }


    override fun onClickMovieExtras(movieExtras: MovieExtras) {
        updateState { state ->
            state.copy(
                extraItem = state.extraItem.map { selectable ->
                    selectable.copy(isSelected = selectable.item == movieExtras)
                }
            )
        }
    }

    override fun onClickShowAllCast() {
        sendNewEffect(MovieDetailsEffect.NavigateToCastsScreenEffect)
    }

    override fun onClickBack() {
        sendNewEffect(MovieDetailsEffect.NavigateBackEffect)
    }

    override fun onClickRetryRequest() {
        loadMovieDetails()
    }

    override fun onRateClicked() {
        viewModelScope.launch {
            runIfLoggedIn(
                onLoggedIn = {},
                onGuest = { showMustLoginDialog(MovieAndSeriesDetailsDialogType.Rate) }
            )
        }
    }

    override fun onNavigateToLoginClicked() {
        sendNewEffect(MovieDetailsEffect.NavigateToLoginScreenEffect)
    }

    override fun onCancelClicked() {
        updateState { it.copy(isLoginDialogVisible = false) }
    }

    override fun onAddToListClicked() {
        viewModelScope.launch {
            runIfLoggedIn(
                onLoggedIn = {},
                onGuest = { showMustLoginDialog(MovieAndSeriesDetailsDialogType.AddToList) }
            )
        }
    }

    private suspend fun runIfLoggedIn(
        onLoggedIn: () -> Unit,
        onGuest: () -> Unit
    ) {
        if (getsSessionType() != SessionType.GUEST) {
            onLoggedIn()
        } else {
            onGuest()
        }
    }

    private fun showMustLoginDialog(dialogType: MovieAndSeriesDetailsDialogType){
        updateState { it.copy(isLoginDialogVisible = true, dialogType = dialogType) }
    }

    private fun onError(exception: AflamiException) {
         when (exception) {
            is NoInternetException -> updateState { it.copy(networkError = true) }
            else -> {}
        }
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }

}