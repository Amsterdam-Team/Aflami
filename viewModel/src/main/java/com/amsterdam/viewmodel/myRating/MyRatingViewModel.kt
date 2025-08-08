package com.amsterdam.viewmodel.myRating

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.myRating.movie.DeleteUserRatedMovieUseCase
import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase
import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.domain.useCase.myRating.tvShow.DeleteUserRatedTvShowUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.viewmodel.myRating.MyRatingUiState.MyRatingErrorState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyRatingViewModel @Inject constructor(
    private val getUserRatedMoviesUseCase: GetUserRatedMoviesUseCase,
    private val deleteUserRatedMoviesUseCase: DeleteUserRatedMovieUseCase,
    private val getUserRatedTvShowsUseCase: GetUserRatedTvShowsUseCase,
    private val deleteUserRatedTvShowUseCase: DeleteUserRatedTvShowUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<MyRatingUiState, MyRatingUiEffect>(MyRatingUiState(), dispatcherProvider),
    MyRatingInteractionListener {

    init {
        getRatedMovies()
    }

    private fun getRatedMovies() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getUserRatedMoviesUseCase.getRatedMovies() },
            onSuccess = ::onGetRatedMoviesSuccess,
            onError = ::onGetRatedMediaError,
            onCompletion = ::onCompletion
        )
    }

    private fun onGetRatedMoviesSuccess(movies: List<UserRatedMovie>) {
        updateState { it.copy(movies = movies.toRatingMovieUiStates(), error = null) }
    }

    private fun getRatedTvShows() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getUserRatedTvShowsUseCase.getRatedTvShows() },
            onSuccess = ::onGetRatedTvShowsSuccess,
            onError = ::onGetRatedMediaError,
            onCompletion = ::onCompletion
        )
    }

    private fun onGetRatedTvShowsSuccess(tvShows: List<UserRatedTvShow>) {
        updateState { it.copy(tvShows = tvShows.toRatingTvShowUiStates(), error = null) }
    }

    private fun onGetRatedMediaError(exception: AflamiException) {
        updateState { it.copy(error = MyRatingErrorState.mapToUiState(exception), isRetryLoading = false) }
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false, isRetryLoading = false) }

    override fun onClickNavigateBack() = sendNewNavigationEffect(MyRatingUiEffect.NavigateBack)

    override fun onChangeTabOption(tabOption: TabOption) {
        if (tabOption == state.value.selectedTabOption) return
        updateState { it.copy(selectedTabOption = tabOption) }.also { refreshRatedContent() }
    }

    override fun onClickMovieCard(movieId: Long) {
        sendNewNavigationEffect(MyRatingUiEffect.NavigateToMovieDetails(movieId))
    }

    override fun onClickDeleteMyMovieRatingIcon(movieId: Long) {
        val updatedMovieList = state.value.movies.filterNot { it.id == movieId }
        updateState { it.copy(movies = updatedMovieList, error = null) }

        tryToExecute(
            action = { deleteUserRatedMoviesUseCase.deleteMovieRate(movieId) },
            onSuccess = ::onDeleteMovieRateSuccess,
            onError = ::onDeleteMovieRateError,
        )
    }

    private fun onDeleteMovieRateSuccess(unit: Unit) {
        sendNewNavigationEffect(MyRatingUiEffect.ShowDeleteRateSuccessSnackBar)
    }

    private fun onDeleteMovieRateError(exception: AflamiException) {
        sendNewNavigationEffect(MyRatingUiEffect.ShowDeleteRateErrorSnackBar)
    }

    override fun onClickTvShowCard(tvShowId: Long) {
        sendNewNavigationEffect(MyRatingUiEffect.NavigateToSeriesDetails(tvShowId))
    }

    override fun onClickDeleteMyTvShowRatingIcon(tvShowId: Long) {
        val updatedTvShowList = state.value.tvShows.filterNot { it.id == tvShowId }
        updateState { it.copy(tvShows = updatedTvShowList, error = null) }

        tryToExecute(
            action = { deleteUserRatedTvShowUseCase.deleteTvShowRate(tvShowId) },
            onSuccess = ::onDeleteTvShowRateSuccess,
            onError = ::onDeleteTvShowRateError,
        )
    }

    private fun onDeleteTvShowRateSuccess(unit: Unit) {
        sendNewEffect(MyRatingUiEffect.ShowDeleteRateSuccessSnackBar)
    }

    private fun onDeleteTvShowRateError(exception: AflamiException) {
        sendNewEffect(MyRatingUiEffect.ShowDeleteRateSuccessSnackBar)
    }


    override fun onClickRetryRequest() {
        updateState { it.copy(isRetryLoading = true) }
        refreshRatedContent()
    }

    fun refreshRatedContent() {
        if (state.value.selectedTabOption == TabOption.MOVIES) getRatedMovies() else getRatedTvShows()
    }
}