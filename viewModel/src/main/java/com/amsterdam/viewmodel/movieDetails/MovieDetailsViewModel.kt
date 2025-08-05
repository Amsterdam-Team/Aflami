package com.amsterdam.viewmodel.movieDetails

import android.util.Log
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
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.amsterdam.viewmodel.shared.BaseViewModel
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
    private val movieDetailsUiStateMapper: MovieDetailsUiStateMapper,
    private val getUserListsUseCase: GetUserListsUseCase,
    private val createListUseCase: CreateNewListUseCase,
    private val getsSessionType: GetsSessionType,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<MovieDetailsUiState, MovieDetailsEffect>(
    MovieDetailsUiState(),
    dispatcherProvider
), MovieDetailsInteractionListener {

    init {
        val movieId = args.movieId!!
        updateState { it.copy(movieId = movieId) }

        manageLocaleLanguageUseCase.getDeviceLanguage()
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

    private fun onGetMovieDetailsSuccess(movieDetails: MovieDetails) =
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
        sendNewNavigationEffect(MovieDetailsEffect.NavigateToCastsScreenEffect)
    }

    override fun onClickBack() {
        sendNewNavigationEffect(MovieDetailsEffect.NavigateBackEffect)
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
        viewModelScope.launch {
            updateState { it.copy(isLoginDialogVisible = false) }
            delay(300)
            sendNewNavigationEffect(MovieDetailsEffect.NavigateToLoginScreenEffect)
        }
    }

    override fun onCancelClicked() {
        updateState {
            it.copy(
                isLoginDialogVisible = false,
                isAddToListDialogVisible = false,
                isCreateNewListDialogVisible = false,
                selectedList = null,
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

    override fun onPlayVideoClicked() {
        sendNewNavigationEffect(MovieDetailsEffect.LaunchMovieVideoEffect(state.value.videoUrl))
    }

    override fun onAddToListClicked() {
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
        movieId: Int,
        listId: Long,
    ) {
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

    override fun onCreateNewListClick() {
        updateState { it.copy(isCreateListLoading = true) }
        tryToExecute(
            action = {
                createListUseCase(state.value.listName)
            },
            onSuccess = { listId ->
                sendNewEffect(MovieDetailsEffect.ListCreatedSuccessfully)
                onSaveMovieToList(state.value.movieId.toInt(), listId.toLong())
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
        Log.e("bk", "onError: $exception")
        when (exception) {
            is NoInternetException -> updateState { it.copy(networkError = true) }
            is NetworkException -> updateState { it.copy(networkError = true) }
            else -> {}
        }
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }

}