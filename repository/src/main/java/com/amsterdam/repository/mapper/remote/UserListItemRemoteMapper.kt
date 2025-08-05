package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.remote.UserListItemDto
import com.amsterdam.repository.mapper.shared.toMovieGenre
import com.amsterdam.repository.utils.toSafeLocalDate

fun UserListItemDto.toMovie(isPoster: Boolean = true): Movie {
    val imageUrl = if (isPoster) fullPosterUrl else fullBackdropUrl
    return Movie(
        id = id,
        name = title.orEmpty(),
        description = overview,
        posterUrl = imageUrl.orEmpty(),
        releaseDate = releaseDate.toSafeLocalDate(),
        rating = voteAverage.toFloat(),
        categories = genreIds.map { toMovieGenre(it.toLong()) },
        popularity = popularity,
        originCountry = originalLanguage,
        runTimeInMinutes = 0
    )
}
