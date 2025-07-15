package com.example.repository.mapper.remote

import com.example.entity.Movie
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse

class MovieRemoteMapper {

    fun mapToMovie(remoteMovieItemDto: RemoteMovieItemDto): Movie {
        return Movie(
            id = remoteMovieItemDto.id,
            name = remoteMovieItemDto.title,
            description = remoteMovieItemDto.overview,
            poster = remoteMovieItemDto.posterPath.orEmpty(),
            productionYear = parseYear(remoteMovieItemDto.releaseDate),
            categories = emptyList(),
            rating = remoteMovieItemDto.voteAverage.toFloat(),
            popularity = remoteMovieItemDto.popularity
        )
    }

    fun mapToMovies(remoteMovieResponse: RemoteMovieResponse): List<Movie> {
        return remoteMovieResponse.results.map { mapToMovie(it) }
    }

    private fun parseYear(date: String): Int {
        return date.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
    }
}