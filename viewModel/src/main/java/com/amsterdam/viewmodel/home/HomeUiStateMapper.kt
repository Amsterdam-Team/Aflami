package com.amsterdam.viewmodel.home

import android.annotation.SuppressLint
import com.amsterdam.entity.Movie
import com.amsterdam.viewmodel.home.HomeUiState.PopularMovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import kotlin.collections.map

class HomeUiStateMapper {

    fun moviesToPopularMoviesUiState(movies: List<Movie>) = movies.map(::movieToPopularMovieUiState)
    fun moviesToMoviesItemsUiState(movies: List<Movie>) = movies.map(::movieToMovieItemUiState)

    @SuppressLint("DefaultLocale")
    private fun movieToPopularMovieUiState(movie: Movie): PopularMovieItemUiState {
        return PopularMovieItemUiState(
            id = movie.id,
            name = movie.name,
            rating = String.format("%.1f", movie.rating),
            posterUrl = movie.posterUrl
        )
    }

    @SuppressLint("DefaultLocale")
    private fun movieToMovieItemUiState(movie: Movie): MovieItemUiState {
        return MovieItemUiState(
            id = movie.id,
            name = movie.name,
            rate = String.format("%.1f", movie.rating),
            posterImageUrl = movie.posterUrl,
            yearOfRelease = movie.releaseDate.year.toString()
        )
    }
}