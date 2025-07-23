package com.example.viewmodel.home

import android.annotation.SuppressLint
import com.example.entity.Movie
import com.example.viewmodel.shared.uiStates.MovieItemUiState
import java.text.DecimalFormat

class HomeUiStateMapper {
        @SuppressLint("DefaultLocale")
        fun toUiState(movies: List<Movie>): HomeUiState {
            return HomeUiState(
                popularMovies = movies.map { movie ->
                    HomeUiState.PopularMovieItemUiState(
                        name = movie.name,
                        rating = String.format("%.1f", movie.rating),
                        posterUrl = movie.posterUrl
                    )
                },
            )
        }

    fun toUpcomingMovieItemUiStates(movies: List<Movie>): List<MovieItemUiState> {
        return movies.map { movie ->
            MovieItemUiState(
                id = movie.id,
                name = movie.name,
                posterImageUrl = movie.posterUrl,
                yearOfRelease = movie.productionYear.toString(),
                rate = DecimalFormat("#.#").format(movie.rating).toString()
            )
        }
    }
}