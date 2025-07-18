package com.example.repository.mapper.local

import com.example.domain.mapper.DomainMapper
import com.example.domain.mapper.DtoMapper
import com.example.entity.Movie
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.relation.MovieWithCategories

class MovieLocalMapper(
    private val categoryLocalMapper: CategoryLocalMapper
) : DomainMapper<Movie, LocalMovieDto>, DtoMapper<Movie, LocalMovieDto> {

    fun toMovie(movieWithCategories: MovieWithCategories): Movie {
        return Movie(
            id = movieWithCategories.movie.movieId,
            name = movieWithCategories.movie.name,
            description = movieWithCategories.movie.description,
            poster = movieWithCategories.movie.poster,
            productionYear = movieWithCategories.movie.productionYear,
            rating = movieWithCategories.movie.rating,
            categories = categoryLocalMapper.toMovieCategories(movieWithCategories.categories),
            popularity = movieWithCategories.movie.popularity,
            originCountry = movieWithCategories.movie.originCountry,
            runTime = movieWithCategories.movie.movieLength,
            hasVideo = movieWithCategories.movie.hasVideo
        )
    }

    fun toMovies(moviesWithCategories: List<MovieWithCategories>): List<Movie> {
        return moviesWithCategories.map { toMovie(it) }
    }

    override fun toDomain(dto: LocalMovieDto): Movie {
        return Movie(
            id = dto.movieId,
            name = dto.name,
            description = dto.description,
            poster = dto.poster,
            productionYear = dto.productionYear,
            rating = dto.rating,
            categories = emptyList(),
            popularity = dto.popularity,
            runTime = dto.movieLength,
            originCountry = dto.originCountry,
            hasVideo = dto.hasVideo
        )
    }

    override fun toDto(domain: Movie): LocalMovieDto {
        return LocalMovieDto(
            movieId = domain.id,
            name = domain.name,
            description = domain.description,
            poster = domain.poster,
            productionYear = domain.productionYear,
            rating = domain.rating,
            popularity = domain.popularity,
            movieLength = domain.runTime,
            originCountry = domain.originCountry,
            hasVideo = domain.hasVideo
        )
    }

}
