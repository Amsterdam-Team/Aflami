package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.mapper.shared.DtoMapper
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class MovieLocalMapper @Inject constructor(): EntityMapper<LocalMovieDto, Movie>,
    DtoMapper<Movie, LocalMovieDto> {
    override fun toEntity(dto: LocalMovieDto): Movie {
        return Movie(
            id = dto.movieId,
            name = dto.name,
            description = dto.description,
            posterUrl = dto.poster,
            releaseDate = dto.releaseDate,
            rating = dto.rating,
            categories = emptyList(),
            popularity = dto.popularity,
            runTimeInMinutes = dto.movieLength,
            originCountry = dto.originCountry,
        )
    }

    override fun toDto(entity: Movie, args: List<Any>): LocalMovieDto {
        return LocalMovieDto(
            movieId = entity.id,
            storedLanguage = args.first().toString(),
            name = entity.name,
            description = entity.description,
            poster = entity.posterUrl,
            releaseDate = entity.releaseDate,
            rating = entity.rating,
            popularity = entity.popularity,
            movieLength = entity.runTimeInMinutes,
            originCountry = entity.originCountry
        )
    }
}
