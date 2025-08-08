package com.amsterdam.viewmodel.myRating

import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.viewmodel.myRating.MyRatingUiState.RatingMovieUiState
import com.amsterdam.viewmodel.myRating.MyRatingUiState.RatingTvShowUiState
import com.amsterdam.viewmodel.utils.toFormattedString

fun UserRatedMovie.toRatingMovieUiState(): RatingMovieUiState {
    return RatingMovieUiState(
        id = movie.id,
        name = movie.name,
        posterImageUrl = movie.posterUrl,
        yearOfRelease = movie.releaseDate.toFormattedString(),
        rate = userRate.toString()
    )
}
fun List<UserRatedMovie>.toRatingMovieUiStates(): List<RatingMovieUiState> {
    return map { it.toRatingMovieUiState() }
}

fun UserRatedTvShow.toRatingTvShowUiState(): RatingTvShowUiState {
    return RatingTvShowUiState(
        id = tvShow.id,
        name = tvShow.name,
        posterImageUrl = tvShow.posterUrl,
        yearOfRelease = tvShow.airDate.toFormattedString(),
        rate = userRate.toString()
    )
}

fun List<UserRatedTvShow>.toRatingTvShowUiStates(): List<RatingTvShowUiState> {
    return map { it.toRatingTvShowUiState() }
}