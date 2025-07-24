package com.example.viewmodel.continueWatching

import android.annotation.SuppressLint
import com.example.entity.Movie
import com.example.viewmodel.shared.uiStates.MovieItemUiState


class ContinueWatchingUiStateMapper {
    @SuppressLint("DefaultLocale")
    fun toUiState(continueWatchingMovies: List<Movie>): ContinueWatchingUiState {
        return ContinueWatchingUiState(
            continueWatchingMovies = continueWatchingMovies.map { movie ->
                MovieItemUiState(
                    id = movie.id,
                    name = movie.name,
                    rate = String.format("%.1f", movie.rating),
                    posterImageUrl = movie.posterUrl,
                    yearOfRelease = movie.releaseDate.toString()
                )
            }
        )
    }
}