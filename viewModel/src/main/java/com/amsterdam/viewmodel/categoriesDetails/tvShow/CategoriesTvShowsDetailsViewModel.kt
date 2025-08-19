package com.amsterdam.viewmodel.categoriesDetails.tvShow

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.details.GetTvShowsByGenreUseCase
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class CategoriesTvShowsDetailsViewModel @Inject constructor(
    private val getTvShowsByGenreUseCase: GetTvShowsByGenreUseCase,
    private val categoriesTvShowsDetailsArgs: CategoriesTvShowsDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<CategoriesTvShowsDetailsUiState, CategoriesTvShowsDetailsUiEffect>(
    CategoriesTvShowsDetailsUiState(),
    dispatcherProvider
), CategoriesTvShowsDetailsInteractionListener {

    init {
        updateState { it.copy(isLoading = true) }
        getInitialGenre()
        loadTvShowsForSelectedGenre()
    }

    override fun onClickBack() {
        sendNewNavigationEffect(CategoriesTvShowsDetailsUiEffect.NavigateBack)
    }

    override fun onClickTvShowCard(tvShowId: Long) {
        sendNewNavigationEffect(CategoriesTvShowsDetailsUiEffect.NavigateToTvShowDetails(tvShowId))
    }

    override fun onClickGenre(tvShowGenre: TvShowGenre) {
        if (state.value.selectedGenre == tvShowGenre) {
            return
        }
        updateUiStateForSelectedGenre(tvShowGenre)
        loadTvShowsForSelectedGenre()
    }

    override fun onClickRetryRequest() {
        loadTvShowsForSelectedGenre()
    }

    override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {
        when (val refreshState = loadStates.refresh) {
            is LoadState.Loading -> {
                resetErrorStateToNull()
                updateState { it.copy(isLoading = true) }
            }

            is LoadState.NotLoading -> updateState { it.copy(isLoading = false) }
            is LoadState.Error -> {
                updateState { it.copy(isLoading = false) }
                onFetchError(refreshState.error as AflamiException)
            }
        }
    }

    private fun onFetchError(exception: AflamiException) {
        updateErrorStateByException(exception)
        updateState { it.copy(isLoading = false) }
    }

    private fun updateUiStateForSelectedGenre(tvShowGenre: TvShowGenre) {
        updateState {
            it.copy(
                selectedGenre = tvShowGenre,
                tvShowGenres = state.value.tvShowGenres.map { genreItem ->
                    genreItem.copy(
                        selectableTvShowGenre = genreItem.selectableTvShowGenre.copy(
                            isSelected = genreItem.selectableTvShowGenre.item == tvShowGenre
                        )
                    )
                })
        }
    }

    private fun loadTvShowsForSelectedGenre() {
        updateState { it.copy(isRetryLoading = true) }
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getTvShowsByGenreUseCase(state.value.selectedGenre, page)
                                    .map { it.toTvShowUiState() }
                        }
                    }
                ).flow.cachedIn(viewModelScope)
            },
            onSuccess = ::onGetTvShowsByGenreSuccess,
            onCompletion = ::onCompletion
        )
    }

    private fun onGetTvShowsByGenreSuccess(tvShows: Flow<PagingData<CategoriesTvShowsDetailsUiState.TvShowsUiState>>) {
        updateState {
            it.copy(tvShows = tvShows)
        }
    }

    private fun getInitialGenre() {
        val initialGenre = TvShowGenre.valueOf(categoriesTvShowsDetailsArgs.genreName)
        updateUiStateForSelectedGenre(initialGenre)
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false, isRetryLoading = false) }

}

