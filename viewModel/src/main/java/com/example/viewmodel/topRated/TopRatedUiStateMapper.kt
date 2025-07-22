package com.example.viewmodel.topRated

import android.annotation.SuppressLint
import com.example.entity.Movie
import com.example.viewmodel.shared.uiStates.MovieItemUiState

class TopRatedUiStateMapper {
    @SuppressLint("DefaultLocale")
    fun toUiState(topRatedMovies: List<Movie>): TopRatedUiState {
        return TopRatedUiState(
            topRatedMovies = moviesToMoviesItemsUiState(topRatedMovies)
        )
    }
    fun moviesToMoviesItemsUiState(movies: List<Movie>) = movies.map(::movieToMovieItemUiState)

    @SuppressLint("DefaultLocale")
    fun movieToMovieItemUiState(movie: Movie): MovieItemUiState {
        return MovieItemUiState(
            id = movie.id,
            name = movie.name,
            rate = String.format("%.1f", movie.rating),
            posterImageUrl = movie.posterUrl,
            yearOfRelease = movie.productionYear.toString()
        )
    }
}