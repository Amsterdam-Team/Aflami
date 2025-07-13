package com.example.repository.mapper.remote

import com.example.entity.Movie
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse

class RemoteMovieMapper {

    fun mapToDomain(dto: RemoteMovieItemDto): Movie {
        return Movie(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            poster = dto.posterPath.orEmpty(),
            productionYear = parseYear(dto.releaseDate),
            categories = emptyList(),
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity
        )
    }

    fun mapResponseToDomain(response: RemoteMovieResponse): List<Movie> {
        return response.results.map { mapToDomain(it) }
    }

    private fun parseYear(date: String): Int {
        return date.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
    }
}