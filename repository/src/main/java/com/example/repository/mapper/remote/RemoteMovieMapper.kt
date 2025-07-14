package com.example.repository.mapper.remote

import com.example.domain.useCase.genreTypes.MovieGenre
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

    fun mapToGenreId(movieGenre: MovieGenre): Int? {
        return when (movieGenre) {
            MovieGenre.ACTION -> 28
            MovieGenre.ADVENTURE -> 12
            MovieGenre.ANIMATION -> 16
            MovieGenre.COMEDY -> 35
            MovieGenre.CRIME -> 80
            MovieGenre.DOCUMENTARY -> 99
            MovieGenre.DRAMA -> 18
            MovieGenre.FAMILY -> 10751
            MovieGenre.FANTASY -> 14
            MovieGenre.HISTORY -> 36
            MovieGenre.HORROR -> 27
            MovieGenre.MUSIC -> 10402
            MovieGenre.MYSTERY -> 9648
            MovieGenre.ROMANCE -> 10749
            MovieGenre.SCIENCE_FICTION -> 878
            MovieGenre.TV_MOVIE -> 10770
            MovieGenre.THRILLER -> 53
            MovieGenre.WAR -> 10752
            MovieGenre.WESTERN -> 37
            MovieGenre.ALL -> null
        }
    }



}