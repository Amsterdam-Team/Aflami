package com.amsterdam.viewmodel.listDetails

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.list.DeleteListUseCase
import com.amsterdam.domain.useCase.list.GetListMediaItemsFromListUseCase
import com.amsterdam.domain.useCase.list.RemoveMovieFromListUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.listDetails.ListDetailsUiState.ListDetailsItemsUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListDetailsViewModel @Inject constructor(
    private val getListMediaItemsFromListUseCase: GetListMediaItemsFromListUseCase,
    private val removeMovieFromListUseCase: RemoveMovieFromListUseCase,
    private val deleteListUseCase: DeleteListUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    args: ListDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ListDetailsUiState, ListDetailsEffect>(
    ListDetailsUiState(),
    dispatcherProvider
), ListDetailsInteractionListener {

    init {
        updateState {
            it.copy(
                listId = args.listId,
                listName = args.listName
            )
        }
        manageLocaleLanguageUseCase.getAppLanguage()
            .onEach { loadListDetails() }
            .launchIn(viewModelScope)

        loadListDetails()
    }

    private var currentPagingSource: PagingSource<ListDetailsItemsUiState>? = null

    private fun loadListDetails() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = {
                        currentPagingSource = PagingSource { page ->
                            getListMediaItemsFromListUseCase(state.value.listId, page)
                                .toListDetailsItemUiState()
                        }
                        currentPagingSource!!
                    }
                ).flow.cachedIn(viewModelScope)
            },
            onSuccess = ::onGetListDetailsSuccess
        )
    }

    private fun onGetListDetailsSuccess(moviesPagingFlow: Flow<PagingData<ListDetailsItemsUiState>>) {
        resetErrorStateToNull()
        updateState { it.copy(listItems = moviesPagingFlow) }
    }

    override fun onClickMovie(movieId: Long) {
        sendNewNavigationEffect(ListDetailsEffect.NavigateToMovieDetailsScreen(movieId))
    }

    override fun onClickTvShow(tvShowId: Long) {
        sendNewNavigationEffect(ListDetailsEffect.NavigateToTvShowDetailsScreen(tvShowId))
    }

    override fun onClickBack() {
        sendNewNavigationEffect(ListDetailsEffect.NavigateBack)
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
        resetErrorStateToNull()
        updateState { it.copy(showDeleteListDialog = false, isDeleteLoading = false,) }
        sendNewNavigationEffect(ListDetailsEffect.NavigateBack)
        sendNewEffect(ListDetailsEffect.ShowDeletionSuccessSnackBar)
    }

    private fun onDeleteListError(exception: AflamiException) {
        viewModelScope.launch {
            updateState { it.copy(showDeleteListDialog = false, isDeleteLoading = false) }
            sendNewEffect(ListDetailsEffect.ShowErrorSnackBar)
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
        resetErrorStateToNull()
        sendNewEffect(ListDetailsEffect.ShowRemoveMovieSuccessSnackBar)
    }

    private fun onRemoveMovieError(exception: AflamiException) {
        viewModelScope.launch {
            updateState { it.copy(
                showDeleteListDialog = false,
                isDeleteLoading = false
            ) }
            sendNewEffect(ListDetailsEffect.ShowErrorSnackBar)
        }
    }

    override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {
        when (val refreshState =loadStates.refresh) {
            is LoadState.Loading -> {
                resetErrorStateToNull()
                updateState { it.copy(isLoading = true) }
            }

            is LoadState.NotLoading -> {
                updateState { it.copy(isLoading = false) }
            }

            is LoadState.Error -> {
                updateState { it.copy(isLoading = false,) }
                updateErrorStateByException(refreshState.error as AflamiException?)
            }
        }
    }
}