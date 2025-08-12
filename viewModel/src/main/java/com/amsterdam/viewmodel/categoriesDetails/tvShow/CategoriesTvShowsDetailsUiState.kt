package com.amsterdam.viewmodel.categoriesDetails.tvShow

import androidx.paging.PagingData
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.shared.defaultTvShowGenres
import com.amsterdam.viewmodel.shared.uiStates.TvShowGenreItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CategoriesTvShowsDetailsUiState(
    val selectedGenre: TvShowGenre= TvShowGenre.COMEDY,
    val tvShowGenres: List<TvShowGenreItemUiState> = defaultTvShowGenres,
    val tvShows: Flow<PagingData<TvShowsUiState>> = emptyFlow(),
    val isLoading: Boolean = false,
    val errorUiState: CategoriesTvShowsDetailsErrorState? = null,
) {
    sealed interface CategoriesTvShowsDetailsErrorState {
        data object NoNetworkConnection : CategoriesTvShowsDetailsErrorState
        data object UnknownError : CategoriesTvShowsDetailsErrorState
        companion object {
            fun toCategoriesTvShowsDetailsErrorState(exception: Throwable): CategoriesTvShowsDetailsErrorState =
                when (exception) {
                    is NetworkException -> NoNetworkConnection
                    else -> UnknownError
                }
        }
    }


    data class TvShowsUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val rate: String = "",
        val yearOfRelease: String = "",
    )
}