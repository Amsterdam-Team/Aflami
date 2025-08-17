package com.amsterdam.repository.mapper

import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.remote.WishListItemRemoteDto
import com.amsterdam.repository.utils.toSafeLocalDate
import kotlinx.datetime.LocalDate

fun WishListItemRemoteDto.toMovieEntity(isPoster: Boolean = true): Movie {
    val imageUrl = if (isPoster) fullPosterUrl else fullBackdropUrl
    return Movie(
        id = id,
        name = title.orEmpty(),
        description = overview,
        posterUrl = imageUrl.orEmpty(),
        releaseDate = releaseDate?.toSafeLocalDate() ?: LocalDate.fromEpochDays(0),
        rating = voteAverage?.toFloat() ?: 0f,
        categories = genreIds?.map { toMovieGenre(it.toLong()) } ?: emptyList(),
        popularity = popularity ?: 0.0,
        originCountry = originalLanguage,
        runTimeInMinutes = 0
    )
}

fun WishListItemRemoteDto.toTvShowEntity(isPoster: Boolean = true): TvShow {
    val imageUrl = if (isPoster) fullPosterUrl else fullBackdropUrl
    return TvShow(
        id = id,
        name = title.orEmpty(),
        description = overview,
        posterUrl = imageUrl.orEmpty(),
        airDate = releaseDate?.toSafeLocalDate() ?: LocalDate.fromEpochDays(0),
        categories = genreIds?.map { toTvShowGenre(it.toLong()) } ?: emptyList(),
        rating = voteAverage?.toFloat() ?: 0f,
        popularity = popularity ?: 0.0,
        seasonCount = 0,
        originCountry = originalLanguage
    )
}