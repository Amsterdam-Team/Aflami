package com.amsterdam.viewmodel.categoriesDetails.movies

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@HiltViewModel
class CategoriesMoviesDetailsViewModel @Inject constructor(
    private val categoriesMoviesDetailsPagingSource: CategoriesMoviesDetailsPagingSource,
    private val categoriesMovieDetailsArgs: CategoriesMovieDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<CategoriesMoviesDetailsUiState, CategoriesMoviesDetailsUiEffect>(
    CategoriesMoviesDetailsUiState(),
    dispatcherProvider
), CategoriesMoviesDetailsInteractionListener {
    init {
        getInitialGenre()
        loadMoviesForSelectedGenre()
    }

    private fun loadMoviesForSelectedGenre() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = {
                categoriesMoviesDetailsPagingSource.getMovies(state.value.selectedGenre)
                    .map { pagingData ->
                        pagingData.map { it.toMovieUiState() }
                    }.cachedIn(viewModelScope)
            },
            onSuccess = ::onGetMoviesByGenreSuccess,
            onCompletion = ::onCompletion
        )
    }

    private fun getInitialGenre() {
        val initialGenre = MovieGenre.valueOf(categoriesMovieDetailsArgs.genreName)
        updateUiStateForSelectedGenre(initialGenre)
    }

    override fun onClickRetryRequest() {
        loadMoviesForSelectedGenre()
    }

    override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {
        when (val refreshState = loadStates.refresh) {
            is LoadState.Loading -> updateState { it.copy(isLoading = true, errorUiState = null) }
            is LoadState.NotLoading -> updateState { it.copy(isLoading = false) }
            is LoadState.Error -> {
                updateState { it.copy(isLoading = false) }
                onFetchError(refreshState.error as AflamiException)
            }
        }
    }

    private fun onFetchError(exception: AflamiException) {
        updateState {
            it.copy(
                errorUiState = CategoriesMoviesDetailsUiState.CategoriesDetailsErrorState
                    .toCategoriesMoviesDetailsErrorState(exception),
                isLoading = false
            )
        }
    }

    override fun onClickBack() {
        sendNewNavigationEffect(CategoriesMoviesDetailsUiEffect.NavigateBack)
    }

    override fun onClickMovieCard(movieId: Long) {
        sendNewNavigationEffect(CategoriesMoviesDetailsUiEffect.NavigateToMovieDetails(movieId))
    }

    override fun onClickGenre(movieGenre: MovieGenre) {
        if (state.value.selectedGenre == movieGenre) {
            return
        }
        updateUiStateForSelectedGenre(movieGenre)
        loadMoviesForSelectedGenre()
    }

    private fun updateUiStateForSelectedGenre(movieGenre: MovieGenre) {
        updateState {
            it.copy(
                selectedGenre = movieGenre,
                movieGenres = state.value.movieGenres.map { genreItem ->
                    genreItem.copy(
                        selectableMovieGenre = genreItem.selectableMovieGenre.copy(
                            isSelected = genreItem.selectableMovieGenre.item == movieGenre
                        )
                    )
                }
            )
        }
    }

    private fun onGetMoviesByGenreSuccess(movies: Flow<PagingData<CategoriesMoviesDetailsUiState.MoviesUiState>>) {
        updateState {
            it.copy(movies = movies)
        }
    }

    private fun onCompletion() = updateState { it.copy(isLoading = false) }
}
