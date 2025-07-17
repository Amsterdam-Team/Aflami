package com.example.repository.mapper.remote

import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.mapper.shared.mapToMovieCategory

class MovieRemoteMapper {

    fun mapToMovie(remoteMovieItemDto: RemoteMovieItemDto): Movie {
        return Movie(
            id = remoteMovieItemDto.id,
            name = remoteMovieItemDto.title,
            description = remoteMovieItemDto.overview,
            posterUrl = remoteMovieItemDto.posterPath.orEmpty(),
            productionYear = parseYear(remoteMovieItemDto.releaseDate),
            categories = mapGenreIdsToCategories(remoteMovieItemDto.genreIds),
            rating = remoteMovieItemDto.voteAverage.toFloat(),
            popularity = remoteMovieItemDto.popularity
        )
    }

    fun mapToMovies(remoteMovieResponse: RemoteMovieResponse): List<Movie> {
        return remoteMovieResponse.results.map { mapToMovie(it) }
    }

    fun mapToLocalMovies(remoteMovieResponse: RemoteMovieResponse): List<LocalMovieDto> {
        return remoteMovieResponse.results.map { mapToLocalMovie(it) }
    }


    private fun mapToLocalMovie(remoteMovieItemDto: RemoteMovieItemDto): LocalMovieDto {
        return LocalMovieDto(
            movieId = remoteMovieItemDto.id,
            name = remoteMovieItemDto.title,
            description = remoteMovieItemDto.overview,
            poster = remoteMovieItemDto.posterPath.orEmpty(),
            productionYear = parseYear(remoteMovieItemDto.releaseDate),
            rating = remoteMovieItemDto.voteAverage.toFloat(),
            popularity = remoteMovieItemDto.popularity
        )
    }

    private fun parseYear(date: String): Int {
        return date.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
    }

    private fun mapGenreIdsToCategories(genreIds: List<Int>): List<MovieGenre> {
        return genreIds.map { it.toLong().mapToMovieCategory() }
    }
}