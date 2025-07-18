package com.example.repository.mapper.local

import com.example.entity.Movie
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.mapper.shared.EntityMapper

class MovieWithCategoriesLocalMapper(
    private val movieGenreLocalMapper: MovieGenreLocalMapper
) : EntityMapper<MovieWithCategories, Movie> {
    override fun toEntity(dto: MovieWithCategories): Movie {
        return Movie(
            id = dto.movie.movieId,
            name = dto.movie.name,
            description = dto.movie.description,
            poster = dto.movie.poster,
            productionYear = dto.movie.productionYear,
            rating = dto.movie.rating,
            categories = movieGenreLocalMapper.toEntityList(dto.categories),
            popularity = dto.movie.popularity,
            originCountry = dto.movie.originCountry,
            runTime = dto.movie.movieLength,
            hasVideo = dto.movie.hasVideo
        )
    }

}