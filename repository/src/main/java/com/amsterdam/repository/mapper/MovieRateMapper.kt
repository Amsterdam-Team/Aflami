package com.amsterdam.repository.mapper

import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto

fun RemoteMovieItemDto.toMovieUserRateEntity(dto: RemoteMovieItemDto): UserRatedMovie {
    return UserRatedMovie(
        movie = dto.toEntity(),
        userRate = rating.toInt()
    )
}

fun List<RemoteMovieItemDto>.toMovieUserRateEntityList(): List<UserRatedMovie> = map { it.toMovieUserRateEntity(it) }