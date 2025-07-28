package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.mapper.shared.mapCategoryIdToMovieGenre
import com.amsterdam.repository.utils.toSafeLocalDate
import javax.inject.Inject

class MovieRemoteMapper @Inject constructor() : EntityMapper<RemoteMovieItemDto, Movie> {

    override fun toEntity(dto: RemoteMovieItemDto): Movie {
        return  toEntity(dto,isPoster = true)
    }

    fun toEntity(dto: RemoteMovieItemDto,isPoster : Boolean): Movie {
        val genresIds = dto.genreIds.ifEmpty { dto.genres.map { it.id } }
        val imageUrl = if (isPoster) dto.fullPosterUrl else dto.fullBackdropUrl
        return Movie(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            posterUrl = imageUrl.orEmpty(),
            releaseDate = dto.releaseDate.toSafeLocalDate(),
            categories = mapGenreIdsToCategories(genresIds),
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity,
            originCountry = dto.originCountry.firstOrNull() ?: "",
            runTimeInMinutes = dto.runtime,
            hasVideo = dto.video
        )
    }

    fun toEntityList(dtoList: List<RemoteMovieItemDto>,isPoster: Boolean): List<Movie> {
        return dtoList.map { toEntity(it, isPoster) }
    }

    private fun mapGenreIdsToCategories(genreIds: List<Int>): List<MovieGenre> {
        return genreIds.map { mapCategoryIdToMovieGenre(it.toLong()) }
    }
}