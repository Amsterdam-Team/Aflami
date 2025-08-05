package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.remote.UserListItemDto
import com.amsterdam.repository.mapper.shared.toMovieGenre
import com.amsterdam.repository.utils.toSafeLocalDate
import kotlinx.datetime.LocalDate

fun UserListItemDto.toMovie(isPoster: Boolean = true): Movie {
    val imageUrl = if (isPoster) fullPosterUrl else fullBackdropUrl
    return Movie(
        id = id,
        name = title.orEmpty(),
        description = overview,
        posterUrl = imageUrl.orEmpty(),
        releaseDate = releaseDate?.toSafeLocalDate() ?: LocalDate.fromEpochDays(0) ,
        rating = voteAverage?.toFloat() ?: 0f,
        categories = genreIds?.map { toMovieGenre(it.toLong()) } ?: emptyList(),
        popularity = popularity ?: 0.0,
        originCountry = originalLanguage,
        runTimeInMinutes = 0
    )
}
