package com.example.repository.mapper.remote

import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import com.example.repository.BuildConfig
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.mapper.shared.EntityMapper
import com.example.repository.mapper.shared.toMovieCategory
import com.example.repository.utils.DateParser

class MovieRemoteMapper(
    private val dateParser: DateParser
) : EntityMapper<RemoteMovieItemDto, Movie> {

    override fun toEntity(dto: RemoteMovieItemDto): Movie {
        val genresIds = dto.genreIds.ifEmpty { dto.genres.map { it.id } }
        return Movie(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            posterUrl = BuildConfig.BASE_IMAGE_URL + dto.posterPath.orEmpty(),
            productionYear = dateParser.parseYear(dto.releaseDate).toUInt(),
            categories = mapGenreIdsToCategories(genresIds),
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity,
            originCountry = dto.originCountry.firstOrNull() ?: "",
            runTime = dto.runtime,
            hasVideo = dto.video
        )
    }

    private fun mapGenreIdsToCategories(genreIds: List<Int>): List<MovieGenre> {
        return genreIds.map { it.toLong().toMovieCategory() }
    }
}