package com.amsterdam.viewmodel.search.mapper

import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState

fun UserRatedMovie.toMovieItemUiState(): MovieItemUiState {
    val movieItemUiState = movie.toMediaItemUiState()
    return movieItemUiState.copy(rate = userRate.toString())
}

fun List<UserRatedMovie>.toRatedMovieUiStates() = map(UserRatedMovie::toMovieItemUiState)
