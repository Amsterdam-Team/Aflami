package com.example.repository.mapper.remote

import com.example.entity.Movie
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.mapper.shared.toMovieCategory

fun RemoteMovieItemDto.toMovie(): Movie {
    return Movie(
        id = id,
        name = title,
        description = overview,
        poster = posterPath.orEmpty(),
        productionYear = releaseDate.parseYear(),
        categories = genreIds.map { it.toLong().toMovieCategory() },
        rating = voteAverage.toFloat(),
        popularity = popularity
    )
}

fun RemoteMovieItemDto.toLocalMovie(): LocalMovieDto {
    return LocalMovieDto(
        movieId = id,
        name = title,
        description = overview,
        poster = posterPath.orEmpty(),
        productionYear = releaseDate.parseYear(),
        rating = voteAverage.toFloat(),
        popularity = popularity
    )
}

fun RemoteMovieResponse.toMovies(): List<Movie> {
    return results.map { it.toMovie() }
}

fun RemoteMovieResponse.toLocalMovies(): List<LocalMovieDto> {
    return results.map { it.toLocalMovie() }
}

private fun String.parseYear(): Int {
    return takeIf { length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
}
