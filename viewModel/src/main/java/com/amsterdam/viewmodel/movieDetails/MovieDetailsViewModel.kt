package com.amsterdam.viewmodel.movieDetails

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase.MovieDetails
import com.amsterdam.domain.useCase.list.AddMovieToListUseCase
import com.amsterdam.domain.useCase.list.CreateNewListUseCase
import com.amsterdam.domain.useCase.list.GetUserListsUseCase
import com.amsterdam.domain.useCase.myRating.movie.SetUserMovieRatingUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.amsterdam.viewmodel.myRating.RateDialogInteractionListener
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.RateDialogUiState
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.MovieAndSeriesDetailsDialogType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    args: MovieDetailsArgs,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val addMovieToListUseCase: AddMovieToListUseCase,
    private val getUserListsUseCase: GetUserListsUseCase,
    private val createListUseCase: CreateNewListUseCase,
    private val getsSessionType: GetsSessionType,
    private val setUserRatingUseCase: SetUserMovieRatingUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(
    MovieDetailsUiState(),
    dispatcherProvider
), MovieDetailsInteractionListener, RateDialogInteractionListener {

    init {
        val movieId = args.movieId
        updateState { it.copy(movieId = movieId) }

        manageLocaleLanguageUseCase.getAppLanguage()
            .onEach {
                loadMovieDetails()
            }.launchIn(viewModelScope)

        loadMovieDetails()
    }

    private fun loadMovieDetails() {
        updateState { it.copy(isLoading = true, networkError = false) }
        tryToExecute(
            action = ::getMovieDetails,
            onSuccess = ::onGetMovieDetailsSuccess,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private suspend fun getMovieDetails() =
        getMovieDetailsUseCase(state.value.movieId)

    private fun onGetMovieDetailsSuccess(movieDetails: MovieDetails) {
        updateState { movieDetails.toUiState() }
    }

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
        sendNewNavigationEffect(MovieDetailsEffect.NavigateToCastsScreenEffect)
    }

    override fun onClickBack() {
        sendNewNavigationEffect(MovieDetailsEffect.NavigateBackEffect)
    }

    override fun onClickRetryRequest() {
        loadMovieDetails()
    }

    override fun onClickRate() {
        viewModelScope.launch {
            runIfLoggedIn(
                onLoggedIn = {
                    val userRate = state.value.rateDialogUiState.selectedStarIndex
                    updateState {
                        it.copy(
                            rateDialogUiState = RateDialogUiState(
                                isVisible = true,
                                isSubmittingEnabled = false,
                                selectedStarIndex = userRate,
                                previousStarIndex = userRate
                            )
                        )
                    }
                },
                onGuest = { showMustLoginDialog(MovieAndSeriesDetailsDialogType.Rate) }
            )
        }
    }

    override fun onClickNavigateToLogin() {
        viewModelScope.launch {
            updateState { it.copy(isLoginDialogVisible = false) }
            delay(300)
            sendNewNavigationEffect(MovieDetailsEffect.NavigateToLoginScreenEffect)
        }
    }

    override fun onClickCancel() {
        updateState {
            it.copy(
                isAddMovieToListLoading = false,
                isLoginDialogVisible = false,
                isAddToListDialogVisible = false,
                isCreateNewListDialogVisible = false,
                selectedList = null,
                listName = "",
            )
        }
    }

    override fun onClickSimilarMovie(movieId: Long) {
        sendNewNavigationEffect(MovieDetailsEffect.NavigateToMovieDetails(movieId))
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

    override fun onClickPlayVideo() {
        sendNewNavigationEffect(MovieDetailsEffect.LaunchMovieVideoEffect(state.value.videoUrl))
    }

    override fun onClickAddToList() {
        if (state.value.isLoading) return
        viewModelScope.launch {
            runIfLoggedIn(
                onLoggedIn = {
                    val userList = getUserListsUseCase()
                    updateState { it.copy(isAddToListDialogVisible = true, userLists = userList.toUiState()) }
                },
                onGuest = { showMustLoginDialog(MovieAndSeriesDetailsDialogType.AddToList) },
            )
        }
    }

    override fun onSaveMovieToList(
        movieId: Long,
        listId: Long,
    ) {
        updateState { it.copy(isAddMovieToListLoading = true) }
        tryToExecute(
            action = { addMovieToListUseCase(movieId = movieId, listId = listId) },
            onSuccess = {
                sendNewNavigationEffect(MovieDetailsEffect.MovieAddedToListSuccessfully)
            },
            onError = {
                it.printStackTrace()
                sendNewNavigationEffect(MovieDetailsEffect.MovieAddedToListError)
            },
            onCompletion = {
                updateState {
                    it.copy(
                        isAddToListDialogVisible = false,
                        isCreateNewListDialogVisible = false,
                        selectedList = null,
                    )
                }
            },
        )
    }

    override fun onClickCreateList() {
        updateState { it.copy(isCreateNewListDialogVisible = true, isAddToListDialogVisible = false) }
    }

    override fun onChangeListName(listName: String) {
        updateState { it.copy(listName = listName) }
    }

    override fun onClickCreateNewList() {
        updateState { it.copy(isCreateListLoading = true) }
        tryToExecute(
            action = {
                createListUseCase(state.value.listName)
            },
            onSuccess = { listId ->
                sendNewEffect(MovieDetailsEffect.ListCreatedSuccessfully)
                onSaveMovieToList(state.value.movieId, listId.toLong())
            },
            onError = {
                sendNewEffect(MovieDetailsEffect.FailedToCreateList)
            },
            onCompletion = {
                updateState {
                    it.copy(
                        isCreateNewListDialogVisible = false,
                        listName = "",
                        isCreateListLoading = false,
                    )
                }
            },
        )
    }

    override fun onSelectedListChange(selectedList: UserListUiState) {
        updateState { it.copy(selectedList = selectedList) }
    }

    private suspend fun runIfLoggedIn(
        onLoggedIn: suspend () -> Unit,
        onGuest: () -> Unit,
    ) {
        if (getsSessionType() != SessionType.GUEST) {
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
            is NoInternetException -> updateState { it.copy(networkError = true) }
            is NetworkException -> updateState { it.copy(networkError = true) }
            else -> {}
        }
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }

    override fun onClickCancelRateDialog() {
        updateState { it.copy(rateDialogUiState = it.rateDialogUiState.copy(isVisible = false, selectedStarIndex = state.value.rateDialogUiState.previousStarIndex)) }
    }

    override fun onClickSubmit() {
        updateState { it.copy(rateDialogUiState = it.rateDialogUiState.copy(isLoading = true)) }
        tryToExecute(
            action = {
                setUserRatingUseCase.setUserMovieRate(
                    rate = state.value.rateDialogUiState.selectedStarIndex ?: 0,
                    movieId = state.value.movieId
                )
            },
            onSuccess = ::onSubmitRateSuccess,
            onError = ::onSubmitRateError
        )
    }

    private fun onSubmitRateSuccess(unit: Unit) {
        updateState { it.copy(rateDialogUiState = it.rateDialogUiState.copy(isVisible = false, isLoading = false)) }
        sendNewEffect(MovieDetailsEffect.ShowRatingSuccessSnackBar)
    }

    private fun onSubmitRateError(exception: AflamiException) {
        updateState { it.copy(rateDialogUiState = it.rateDialogUiState.copy(isVisible = false, isLoading = false, selectedStarIndex = it.rateDialogUiState.previousStarIndex)) }
        sendNewEffect(MovieDetailsEffect.ShowRatingErrorSnackBar)
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