package com.amsterdam.viewmodel.listDetails

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.list.DeleteListUseCase
import com.amsterdam.domain.useCase.list.GetMoviesFromListUseCase
import com.amsterdam.domain.useCase.list.RemoveMovieFromListUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListDetailsViewModel @Inject constructor(
    private val getMoviesFromListUseCase: GetMoviesFromListUseCase,
    private val removeMovieFromListUseCase: RemoveMovieFromListUseCase,
    private val deleteListUseCase: DeleteListUseCase,
    private val listDetailsUiStateMapper: ListDetailsUiStateMapper,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    args: ListDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ListDetailsUiState, ListDetailsEffect>(
    ListDetailsUiState(),
    dispatcherProvider
), ListDetailsInteractionListener {

    init {
        updateState { it.copy(
            listId = args.listId,
            listName = args.listName
        ) }
        manageLocaleLanguageUseCase.getDeviceLanguage()
            .onEach { loadListDetails() }
            .launchIn(viewModelScope)

        loadListDetails()
    }

    private var currentPagingSource: PagingSource<MovieItemUiState>?  = null

    private fun loadListDetails() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = {
                        currentPagingSource = PagingSource { page ->
                            getMoviesFromListUseCase(state.value.listId, page)
                                .map { listDetailsUiStateMapper.movieToMovieItemUiState(it) }
                        }
                        currentPagingSource!!
                    }
                ).flow.cachedIn(viewModelScope)
            },
            onSuccess = ::onGetListDetailsSuccess,
            onError = {},
            onCompletion = ::onGetListDetailsCompletion
        )
    }

    private fun onGetListDetailsSuccess(moviesPagingFlow: Flow<PagingData<MovieItemUiState>>) {
        updateState { it.copy(listItems = moviesPagingFlow) }
    }

    private fun onGetListDetailsCompletion() = updateState { it.copy(isLoading = false) }

    override fun onClickMovie(movieId: Long) {
        sendNewEffect(ListDetailsEffect.NavigateToMovieDetailsScreen(movieId))
    }

    override fun onClickBack() {
        sendNewEffect(ListDetailsEffect.NavigateBack)
    }

    override fun onClickRetryLoading() {
        loadListDetails()
    }

    override fun onClickDeleteList() {
        updateState { it.copy(showDeleteListDialog = true) }
    }

    override fun onDeleteListDialogDismiss() {
        updateState { it.copy(showDeleteListDialog = false) }
    }

    override fun onDeleteListConfirmed() {
        updateState { it.copy(isDeleteLoading = true) }
        tryToExecute(
            action = { deleteListUseCase(state.value.listId) },
            onSuccess = { onDeleteListSuccess() },
            onError = ::onDeleteListError
        )
    }

    private fun onDeleteListSuccess() {
        updateState { it.copy(
            showDeleteListDialog = false,
            isDeleteLoading = false,
            error = null
        ) }
        sendNewNavigationEffect(ListDetailsEffect.NavigateBack)
        sendNewEffect(ListDetailsEffect.ShowDeletionSuccessSnackBar)
    }

    private fun onDeleteListError(exception: AflamiException) {
        val error = ListDetailsError.toListDetailsError(exception)
        viewModelScope.launch {
            updateState { it.copy(
                error = error,
                showDeleteListDialog = false,
                isDeleteLoading = false
            ) }
            sendNewEffect(ListDetailsEffect.ShowErrorSnackbar(error = state.value.error))
        }
    }

    override fun onClickRemoveMovie(movieId: Long) {
        tryToExecute(
            action = { removeMovieFromListUseCase(state.value.listId, movieId) },
            onSuccess = { onRemoveMovieSuccess() },
            onError = ::onRemoveMovieError
        )
    }

    private fun onRemoveMovieSuccess() {
        currentPagingSource?.invalidate()
        updateState { it.copy(error = null) }
        sendNewEffect(ListDetailsEffect.ShowRemoveMovieSuccessSnackBar)
    }

    private fun onRemoveMovieError(exception: AflamiException) {
        val error = ListDetailsError.toListDetailsError(exception)
        viewModelScope.launch {
            updateState { it.copy(
                error = error,
                showDeleteListDialog = false,
                isDeleteLoading = false
            ) }
            sendNewEffect(ListDetailsEffect.ShowErrorSnackbar(error = state.value.error))
        }
    }
}