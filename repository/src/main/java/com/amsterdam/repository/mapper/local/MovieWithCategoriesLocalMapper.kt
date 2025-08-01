package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class MovieWithCategoriesLocalMapper @Inject constructor(
    private val movieGenreLocalMapper: MovieGenreLocalMapper
) : EntityMapper<MovieWithCategories, Movie> {
    override fun toEntity(dto: MovieWithCategories): Movie {
        return Movie(
            id = dto.movie.movieId,
            name = dto.movie.name,
            description = dto.movie.description,
            posterUrl = dto.movie.poster,
            releaseDate = dto.movie.releaseDate,
            rating = dto.movie.rating,
            categories = movieGenreLocalMapper.toEntityList(dto.categories),
            popularity = dto.movie.popularity,
            originCountry = dto.movie.originCountry,
            runTimeInMinutes = dto.movie.movieLength,
            hasVideo = dto.movie.hasVideo,
            productionCompanies = emptyList()
        )
    }

}