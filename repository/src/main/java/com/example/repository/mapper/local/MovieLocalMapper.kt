package com.example.repository.mapper.local

import com.example.entity.Movie
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.mapper.shared.DtoMapper
import com.example.repository.mapper.shared.EntityMapper

class MovieLocalMapper : EntityMapper<LocalMovieDto, Movie>,
    DtoMapper<Movie, LocalMovieDto> {
    override fun toEntity(dto: LocalMovieDto): Movie {
        return Movie(
            id = dto.movieId,
            name = dto.name,
            description = dto.description,
            posterUrl = dto.poster,
            productionYear = dto.productionYear.toUInt(),
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
            poster = domain.posterUrl,
            productionYear = domain.productionYear.toInt(),
            rating = domain.rating,
            popularity = domain.popularity,
            movieLength = domain.runTime,
            originCountry = domain.originCountry,
            hasVideo = domain.hasVideo
        )
    }
}
