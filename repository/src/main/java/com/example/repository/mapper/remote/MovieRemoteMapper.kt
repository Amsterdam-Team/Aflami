package com.example.repository.mapper.remote

import com.example.domain.mapper.DomainMapper
import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import com.example.repository.BuildConfig
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.example.repository.mapper.shared.toMovieCategory

class MovieRemoteMapper: DomainMapper<Movie, RemoteMovieItemDto> {

    fun toMovies(remoteMovieResponse: RemoteMovieResponse): List<Movie> {
        return remoteMovieResponse.results.map { toDomain(it) }
    }

    fun toLocalMovies(remoteMovieResponse: RemoteMovieResponse): List<LocalMovieDto> {
        return remoteMovieResponse.results.map { toLocalMovie(it) }
    }


    private fun toLocalMovie(remoteMovieItemDto: RemoteMovieItemDto): LocalMovieDto {
        return LocalMovieDto(
            movieId = remoteMovieItemDto.id,
            name = remoteMovieItemDto.title,
            description = remoteMovieItemDto.overview,
            poster = remoteMovieItemDto.posterPath.orEmpty(),
            productionYear = parseYear(remoteMovieItemDto.releaseDate),
            rating = remoteMovieItemDto.voteAverage.toFloat(),
            popularity = remoteMovieItemDto.popularity,
            movieLength = remoteMovieItemDto.runtime,
            originCountry = remoteMovieItemDto.originCountry.firstOrNull() ?: "",
            hasVideo = remoteMovieItemDto.video
        )
    }

    private fun parseYear(date: String): Int {
        return date.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
    }

    private fun mapGenreIdsToCategories(genreIds: List<Int>): List<MovieGenre> {
        return genreIds.map { it.toLong().toMovieCategory() }
    }

    override fun toDomain(dto: RemoteMovieItemDto): Movie {
        val genresIds = if (dto.genreIds.isNotEmpty())
            dto.genreIds
        else dto.genres.map { it.id }
        return Movie(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            poster = BuildConfig.BASE_IMAGE_URL + dto.posterPath.orEmpty(),
            productionYear = parseYear(dto.releaseDate),
            categories = mapGenreIdsToCategories(genresIds),
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity,
            originCountry = dto.originCountry.firstOrNull() ?: "",
            runTime = dto.runtime,
            hasVideo = dto.video
        )
    }
}