package com.amsterdam.viewmodel.movieDetails

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase.MovieDetails
import com.amsterdam.domain.useCase.list.AddMovieToListUseCase
import com.amsterdam.domain.useCase.list.CheckIsMovieInListUseCase
import com.amsterdam.domain.useCase.list.CreateNewListUseCase
import com.amsterdam.domain.useCase.list.GetWishListsUseCase
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
    private val getWishListsUseCase: GetWishListsUseCase,
    private val createListUseCase: CreateNewListUseCase,
    private val getsSessionType: GetsSessionType,
    private val setUserRatingUseCase: SetUserMovieRatingUseCase,
    private val checkIsMovieInListUseCase: CheckIsMovieInListUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(
    MovieDetailsUiState(),
    dispatcherProvider
), MovieDetailsInteractionListener, RateDialogInteractionListener {

    init {
        val movieId = args.movieId
        updateState { it.copy(movieId = movieId) }

        manageLocaleLanguageUseCase.getAppLanguage()
            .onEach { language ->
                updateState { it.copy(currentLanguage = language.value) }
                getMovieDetails()
                loadWishLists()
            }.launchIn(viewModelScope)
    }

    private fun getMovieDetails() {
        resetErrorStateToNull()
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = { getMovieDetailsUseCase(state.value.movieId) },
            onSuccess = ::onGetMovieDetailsSuccess,
            onCompletion = ::onGetMovieDetailsCompletion
        )
    }

    private fun onGetMovieDetailsCompletion() = updateState { it.copy(isLoading = false) }
    private fun loadWishLists() {
        tryToExecute(
            action = ::getWishLists,
            onCompletion = ::onGetWishListsComplete
        )
    }

    private suspend fun getWishLists() {
        updateState {
            it.copy(
                isUserListsLoading = true,
            )
        }
        runIfLoggedIn(
            onLoggedIn = {
                val list = getWishListsUseCase().toUiState()
                val userLists = list
                    .map { lists ->
                        lists.copy(
                            isMovieInList = checkIsMovieInListUseCase(
                                movieId = state.value.movieId,
                                listId = lists.id
                            )
                        )
                    }
                updateState {
                    it.copy(
                        userLists = userLists,
                    )
                }
            },
        )
    }

    private fun onGetWishListsComplete() {
        updateState {
            it.copy(
                isUserListsLoading = false
            )
        }
    }

    private fun onGetMovieDetailsSuccess(movieDetails: MovieDetails) {
        updateState { movieDetails.toUiState(it) }
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
        getMovieDetails()
        loadWishLists()
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
                selectedLists = emptyList(),
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
                    updateState {
                        it.copy(
                            isAddToListDialogVisible = true,
                        )
                    }
                },
                onGuest = { showMustLoginDialog(MovieAndSeriesDetailsDialogType.AddToList) },
            )
        }
    }

    override fun onSaveMovieToList(
        movieId: Long,
        listIds: List<Long>,
    ) {
        updateState { it.copy(isAddMovieToListLoading = true) }
        tryToExecute(
            action = {
                listIds.forEach { listId ->
                    addMovieToListUseCase(movieId = movieId, listId = listId)
                }
            },
            onSuccess = {
                sendNewEffect(MovieDetailsEffect.MovieAddedToListSuccessfully)
                setListToAdded(listIds)
            },
            onError = {
                it.printStackTrace()
                sendNewEffect(MovieDetailsEffect.MovieAddedToListError)
            },
            onCompletion = {
                updateState {
                    it.copy(
                        isAddToListDialogVisible = false,
                        isCreateNewListDialogVisible = false,
                        isAddMovieToListLoading = false,
                        selectedLists = emptyList(),
                    )
                }
            },
        )
    }

    private fun setListToAdded(listIds: List<Long>) {
        val ids = listIds.toHashSet()
        updateState { state ->
            state.copy(
                userLists = state.userLists.map { list ->
                    if (list.id in ids) {
                        list.copy(isMovieInList = true, itemCount = list.itemCount + 1)
                    } else {
                        list
                    }
                }
            )
        }
    }

    override fun onClickCreateList() {
        updateState {
            it.copy(
                isCreateNewListDialogVisible = true,
                isAddToListDialogVisible = false
            )
        }
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
                onSaveMovieToList(state.value.movieId, listOf(listId.toLong()))
                loadWishLists()
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

    override fun onSelectedListChange(selectedLists: List<WishListUiState>) {
        updateState { it.copy(selectedLists = selectedLists) }
    }

    private suspend fun runIfLoggedIn(
        onLoggedIn: suspend () -> Unit,
        onGuest: () -> Unit = {},
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

    override fun onClickCancelRateDialog() {
        updateState {
            it.copy(
                rateDialogUiState = it.rateDialogUiState.copy(
                    isVisible = false,
                    selectedStarIndex = state.value.rateDialogUiState.previousStarIndex
                )
            )
        }
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
        updateState {
            it.copy(
                rateDialogUiState = it.rateDialogUiState.copy(
                    isVisible = false,
                    isLoading = false
                )
            )
        }
        sendNewEffect(MovieDetailsEffect.ShowRatingSuccessSnackBar)
    }

    private fun onSubmitRateError(exception: AflamiException) {
        updateState {
            it.copy(
                rateDialogUiState = it.rateDialogUiState.copy(
                    isVisible = false,
                    isLoading = false,
                    selectedStarIndex = it.rateDialogUiState.previousStarIndex
                )
            )
        }
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