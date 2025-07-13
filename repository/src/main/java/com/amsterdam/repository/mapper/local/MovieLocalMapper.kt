package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories

class MovieLocalMapper(
    private val categoryLocalMapper: CategoryLocalMapper
) {

    fun mapFromLocal(dto: MovieWithCategories): Movie {
        return Movie(
            id = dto.movie.movieId,
            name = dto.movie.name,
            description = dto.movie.description,
            poster = dto.movie.poster,
            productionYear = dto.movie.productionYear,
            rating = dto.movie.rating,
            categories = categoryLocalMapper.mapListFromMovieLocal(dto.categories),
            popularity = dto.movie.popularity
        )
    }

    fun mapToLocal(domain: Movie): LocalMovieDto {
        return LocalMovieDto(
            movieId = domain.id,
            name = domain.name,
            description = domain.description,
            poster = domain.poster,
            productionYear = domain.productionYear,
            rating = domain.rating,
            popularity = domain.popularity
        )
    }

    fun mapListFromLocal(dtoList: List<MovieWithCategories>): List<Movie> {
        return dtoList.map { mapFromLocal(it) }
    }

    fun mapListToLocal(domains: List<Movie>): List<LocalMovieDto> {
        return domains.map { mapToLocal(it) }
    }
}
