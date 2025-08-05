package com.amsterdam.viewmodel.seriesDetails

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.details.GetEpisodesBySeasonNumberUseCase
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase.TvShowDetails
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.domain.useCase.myRating.tvShow.SetUserTvShowRatingUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.entity.Episode
import com.amsterdam.viewmodel.myRating.RateDialogInteractionListener
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.MovieAndSeriesDetailsDialogType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesDetailsViewModel @Inject constructor(
    args: SeriesDetailsArgs,
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase,
    private val getEpisodesBySeasonNumberUseCase: GetEpisodesBySeasonNumberUseCase,
    private val seriesDetailsStateMapper: SeriesDetailsStateMapper,
    private val getsSessionType: GetsSessionType,
    private val getUserRatedTvShowsUseCase: GetUserRatedTvShowsUseCase,
    private val setUserTvShowRatingUseCase: SetUserTvShowRatingUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<SeriesDetailsUiState, SeriesDetailsEffect>(
    SeriesDetailsUiState(),
    dispatcherProvider
), SeriesDetailsInteractionListener, RateDialogInteractionListener {

    init {
        val tvShowId = args.tvShowId!!
        updateState { it.copy(tvShowId = tvShowId) }

        manageLocaleLanguageUseCase.getAppLanguage()
            .onEach {
                loadTvShowDetails()
            }.launchIn(viewModelScope)
    }

    private fun loadTvShowDetails() {
        updateState { it.copy(isLoading = true, networkError = false) }
        tryToExecute(
            action = ::getTvShowDetails,
            onSuccess = ::onGetTvShowDetailsSuccess,
            onError = ::onError,
        )
    }

    private suspend fun getTvShowDetails(): TvShowDetails {
        return getTvShowDetailsUseCase(state.value.tvShowId)
    }

    private fun onGetTvShowDetailsSuccess(tvShowDetails: TvShowDetails) {
        updateState {
            seriesDetailsStateMapper.toUiState(tvShowDetails)
        }
    }

    override fun onClickSeriesExtraItem(seriesExtras: SeriesExtras) {
        updateState { state ->
            state.copy(
                extraItem = state.extraItem.map { selectable ->
                    selectable.copy(isSelected = selectable.item == seriesExtras)
                }
            )
        }
    }

    override fun onNavigateBack() {
        sendNewNavigationEffect(SeriesDetailsEffect.NavigateBack)
    }

    override fun onClickRetryButton() {
        loadTvShowDetails()
    }

    override fun onClickShowAllCast() {
        sendNewNavigationEffect(SeriesDetailsEffect.NavigateToCastScreen)
    }

    override fun onAddToListClicked() {
        viewModelScope.launch {
            runIfLoggedIn(
                onLoggedIn = {},
                onGuest = { showMustLoginDialog(MovieAndSeriesDetailsDialogType.AddToList) }
            )
        }
    }

    override fun onRateClicked() {
        viewModelScope.launch {
            runIfLoggedIn(
                onLoggedIn = {
                    updateState { it.copy(rateDialogUiState = it.rateDialogUiState.copy(isVisible = true, isLoading = true)) }
                    tryToExecute(
                        action = { getUserRatedTvShowsUseCase.getRatedTvShow(state.value.tvShowId) },
                        onSuccess = ::onGetRatedTvShowSuccess,
                        onError = ::onGetRatedTvShowError
                    )
                },
                onGuest = { showMustLoginDialog(MovieAndSeriesDetailsDialogType.Rate) }
            )
        }
    }

    private fun onGetRatedTvShowSuccess(tvShow: UserRatedTvShow?) {
        tvShow?.let { ratedMovie ->
            updateState {
                it.copy(
                    rateDialogUiState = it.rateDialogUiState.copy(
                        selectedStarIndex = ratedMovie.userRate.toInt(),
                        previousStarIndex = ratedMovie.userRate.toInt(),
                        isLoading = false,
                        isSubmittingEnabled = false
                    )
                )
            }
        }
    }

    private fun onGetRatedTvShowError(exception: AflamiException) {
        updateState { it.copy(rateDialogUiState = it.rateDialogUiState.copy(selectedStarIndex = null, isLoading = false, isSubmittingEnabled = true))}
    }

    override fun onClickSeasonMenu(seasonNumber: Int) {
        tryToExecute(
            action = { getEpisodesForSeason(seasonNumber) },
            onSuccess = { episodes -> onGetEpisodesSuccess(seasonNumber, episodes) },
            onError = ::onError,
        )
    }

    override fun onNavigateToLoginClicked() {
        sendNewNavigationEffect(SeriesDetailsEffect.NavigateToLoginScreenEffect)
    }

    override fun onCancelClicked() {
        updateState { it.copy(isLoginDialogVisible = false) }
    }

    override fun onClickSimilarMovie(movieId: Long) {
        sendNewNavigationEffect(SeriesDetailsEffect.NavigateToSeriesDetails(movieId))
    }

    override fun onDescriptionExpansionToggled() {
        updateState { it.copy(isDescriptionExpanded = !it.isDescriptionExpanded) }
    }

    override fun onReviewExpansionToggled(reviewId: String) {
        updateState { state ->
            val updatedReviews = state.reviews.map { review ->
                if (review.username == reviewId) {
                    review.copy(isExpanded = !review.isExpanded)
                } else {
                    review
                }
            }
            state.copy(reviews = updatedReviews)
        }
    }

    override fun onPlayVideoClicked() {
        sendNewNavigationEffect(SeriesDetailsEffect.LaunchSeriesVideoEffect(state.value.videoUrl))
    }

    private suspend fun getEpisodesForSeason(seasonNumber: Int): List<Episode> {
        val updatedSeasons = state.value.seasons.map {
            if (it.seasonNumber == seasonNumber && it.episodes.isNotEmpty()) {
                it.copy(isExpanded = !it.isExpanded)
            } else {
                it
            }
        }
        if (updatedSeasons != state.value.seasons) {
            updateState { it.copy(seasons = updatedSeasons) }
            return emptyList()
        }
        return getEpisodesBySeasonNumberUseCase(state.value.tvShowId, seasonNumber)
    }

    private fun onGetEpisodesSuccess(seasonNumber: Int, episodes: List<Episode>) {
        if (episodes.isEmpty()) {
            return
        }
        val updatedSeasons = state.value.seasons.map {
            if (it.seasonNumber == seasonNumber) {
                it.copy(
                    episodes = seriesDetailsStateMapper.mapToEpisodeUiState(episodes),
                    isExpanded = true
                )
            } else {
                it
            }
        }
        updateState {
            it.copy(seasons = updatedSeasons)
        }
    }

    private suspend fun runIfLoggedIn(
        onLoggedIn: () -> Unit,
        onGuest: () -> Unit
    ) {
        if (getsSessionType() == SessionType.LOGGED_IN) {
            onLoggedIn()
        } else {
            onGuest()
        }
    }

    private fun showMustLoginDialog(dialogType: MovieAndSeriesDetailsDialogType) {
        updateState { it.copy(isLoginDialogVisible = true, dialogType = dialogType) }
    }

    private fun onError(exception: AflamiException) {
        when (exception) {
            is NoInternetException -> updateState {
                it.copy(
                    isLoading = false,
                    networkError = true
                )
            }
            is NetworkException -> updateState {
                it.copy(
                    isLoading = false,
                    networkError = true
                )
            }

            else -> {}
        }
    }

    override fun onClickCancelRateDialog() {
        updateState { it.copy(rateDialogUiState = it.rateDialogUiState.copy(isVisible = false)) }
    }

    override fun onClickSubmit() {
        updateState { it.copy(rateDialogUiState = it.rateDialogUiState.copy(isLoading = true)) }
        tryToExecute(
            action = {
                setUserTvShowRatingUseCase.setUserMovieRate(
                    rate = state.value.rateDialogUiState.selectedStarIndex ?: 1,
                    tvShowId = state.value.tvShowId
                )
            },
            onSuccess = ::onSubmitRateSuccess,
            onError = ::onSubmitRateError
        )
    }

    private fun onSubmitRateSuccess(unit: Unit) {
        resetRateDialogUiState()
        sendNewNavigationEffect(SeriesDetailsEffect.ShowRatingSuccessSnackBar)
    }

    private fun onSubmitRateError(exception: AflamiException) {
        resetRateDialogUiState()
        sendNewNavigationEffect(SeriesDetailsEffect.ShowRatingErrorSnackBar)
    }

    private fun resetRateDialogUiState(){
        updateState {
            it.copy(
                rateDialogUiState = it.rateDialogUiState.copy(
                    isLoading = false,
                    isVisible = false,
                    previousStarIndex = null,
                    selectedStarIndex = null,
                    isSubmittingEnabled = false
                )
            )
        }
    }

    override fun onChangeRating(newRate: Int) {
        val previousRate = state.value.rateDialogUiState.previousStarIndex
        val isChanged = newRate != previousRate

        updateState {
            it.copy(
                rateDialogUiState = it.rateDialogUiState.copy(
                    selectedStarIndex = newRate,
                    isSubmittingEnabled = isChanged,
                    isLoading = false
                )
            )
        }
    }
}