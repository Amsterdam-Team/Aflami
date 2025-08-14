package com.amsterdam.repository.mapper

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.utils.toSafeLocalDate

fun MovieLocalDto.toEntity(): Movie =
    Movie(
        id = movieId,
        name = name,
        description = description,
        posterUrl = poster,
        releaseDate = releaseDate,
        rating = rating,
        categories = emptyList(),
        popularity = popularity,
        runTimeInMinutes = movieLength,
        originCountry = originCountry
    )

fun RemoteMovieItemDto.toEntity(
    isPoster: Boolean = true,
    videoUrl: String = ""
): Movie {
    val genreIds = genreIds.ifEmpty { genres.map { it.id } }
    val imageUrl = if (isPoster) fullPosterUrl else fullBackdropUrl

    return Movie(
        id = id,
        name = title,
        description = overview,
        posterUrl = imageUrl.orEmpty(),
        releaseDate = releaseDate.toSafeLocalDate(),
        categories = genreIds.map { toMovieGenre(it.toLong()) },
        rating = voteAverage.toFloat(),
        popularity = popularity,
        originCountry = originCountry.firstOrNull() ?: "",
        runTimeInMinutes = runtime,
        videoUrl = videoUrl
    )
}


fun List<RemoteMovieItemDto>.toMovieEntityList(isPoster: Boolean = true): List<Movie> =
    map { it.toEntity(isPoster) }


fun RemoteMovieItemDto.toLocalDto(isPoster : Boolean = true, storedLanguage: String): MovieLocalDto {
    val imageUrl = if (isPoster) fullPosterUrl else fullBackdropUrl

    return MovieLocalDto(
        movieId = id,
        storedLanguage = storedLanguage,
        name = title,
        description = overview,
        poster = imageUrl.orEmpty(),
        releaseDate = releaseDate.toSafeLocalDate(),
        rating = voteAverage.toFloat(),
        popularity = popularity,
        movieLength = runtime,
        originCountry = originCountry.firstOrNull() ?: ""
    )
}

fun List<RemoteMovieItemDto>.toLocalMovieDtoList(isPoster : Boolean = true,storedLanguage: String) =
    map { it.toLocalDto(isPoster,storedLanguage) }