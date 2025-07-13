package com.example.repository.mapper.remote

import com.example.entity.GenreType
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

    fun mapToGenreId(genreType: GenreType): Int? {
        return when (genreType) {
            GenreType.ACTION -> 28
            GenreType.ADVENTURE -> 12
            GenreType.ANIMATION -> 16
            GenreType.COMEDY -> 35
            GenreType.CRIME -> 80
            GenreType.DOCUMENTARY -> 99
            GenreType.DRAMA -> 18
            GenreType.FAMILY -> 10751
            GenreType.FANTASY -> 14
            GenreType.HISTORY -> 36
            GenreType.HORROR -> 27
            GenreType.MUSIC -> 10402
            GenreType.MYSTERY -> 9648
            GenreType.ROMANCE -> 10749
            GenreType.SCIENCE_FICTION -> 878
            GenreType.TV_MOVIE -> 10770
            GenreType.THRILLER -> 53
            GenreType.WAR -> 10752
            GenreType.WESTERN -> 37
            GenreType.ALL -> null
        }
    }



}