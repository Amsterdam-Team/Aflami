package com.amsterdam.viewmodel.listDetails

import android.annotation.SuppressLint
import com.amsterdam.entity.Movie
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import javax.inject.Inject

class ListDetailsUiStateMapper @Inject constructor() {

    @SuppressLint("DefaultLocale")
    fun movieToMovieItemUiState(movie: Movie): MovieItemUiState {
        return MovieItemUiState(
            id = movie.id,
            name = movie.name,
            rate = String.format("%.1f", movie.rating),
            posterImageUrl = movie.posterUrl,
            yearOfRelease = movie.releaseDate.year.toString()
        )
    }
}