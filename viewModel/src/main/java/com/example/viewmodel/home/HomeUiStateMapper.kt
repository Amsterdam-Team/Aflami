package com.example.viewmodel.home

import android.annotation.SuppressLint
import com.example.entity.Movie

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
}