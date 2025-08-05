package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import kotlin.collections.map

fun RemoteMovieItemDto.toMovieUserRateEntity(dto: RemoteMovieItemDto): UserRatedMovie {
    return UserRatedMovie(
        movie = dto.toEntity(),
        userRate = rating.toInt()
    )
}

fun List<RemoteMovieItemDto>.toMovieUserRateEntityList(): List<UserRatedMovie> = map { it.toMovieUserRateEntity(it) }