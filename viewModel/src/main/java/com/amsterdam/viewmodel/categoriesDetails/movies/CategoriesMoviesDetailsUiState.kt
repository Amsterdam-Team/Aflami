package com.amsterdam.viewmodel.categoriesDetails.movies

import androidx.paging.PagingData
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.shared.defaultMovieGenres
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.shared.uiStates.MovieGenreItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


data class CategoriesMoviesDetailsUiState(
    val selectedGenre: MovieGenre = MovieGenre.COMEDY,
    val movieGenres: List<MovieGenreItemUiState> = defaultMovieGenres,
    val movies: Flow<PagingData<MoviesUiState>> = emptyFlow(),
    val isLoading: Boolean = false,
    val errorUiState: CategoriesDetailsErrorState? = null,
) {
    data class MoviesUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val rate: String = "",
        val yearOfRelease: String = "",
        val mediaType: MediaType = MediaType.MOVIE
    )
    sealed interface CategoriesDetailsErrorState {
        data object NoNetworkConnection : CategoriesDetailsErrorState
        data object UnknownError : CategoriesDetailsErrorState
        companion object {
            fun toCategoriesMoviesDetailsErrorState(exception: Throwable): CategoriesDetailsErrorState =
                when (exception) {
                    is NetworkException -> NoNetworkConnection
                    else -> UnknownError
                }
        }

    }
}