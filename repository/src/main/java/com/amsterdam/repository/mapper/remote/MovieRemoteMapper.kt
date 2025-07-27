package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.mapper.shared.mapCategoryIdToMovieGenre
import com.amsterdam.repository.utils.toSafeLocalDate

class MovieRemoteMapper() : EntityMapper<RemoteMovieItemDto, Movie> {
    override fun toEntity(dto: RemoteMovieItemDto): Movie {
        val genresIds = dto.genreIds.ifEmpty { dto.genres.map { it.id } }
        return Movie(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            posterUrl = dto.fullPosterUrl.orEmpty(),
            releaseDate = dto.releaseDate.toSafeLocalDate(),
            categories = mapGenreIdsToCategories(genresIds),
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity,
            originCountry = dto.originCountry.firstOrNull() ?: "",
            runTimeInMinutes = dto.runtime,
            hasVideo = dto.video
        )
    }

    private fun mapGenreIdsToCategories(genreIds: List<Int>): List<MovieGenre> {
        return genreIds.map { mapCategoryIdToMovieGenre(it.toLong()) }
    }
}