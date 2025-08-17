package com.amsterdam.repository.mapper

import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto

fun MovieItemRemoteDto.toMovieUserRateEntity(dto: MovieItemRemoteDto): UserRatedMovie {
    return UserRatedMovie(
        movie = dto.toEntity(),
        userRate = rating.toInt()
    )
}

fun List<MovieItemRemoteDto>.toMovieUserRateEntityList(): List<UserRatedMovie> {
    return map { it.toMovieUserRateEntity(it) }
}