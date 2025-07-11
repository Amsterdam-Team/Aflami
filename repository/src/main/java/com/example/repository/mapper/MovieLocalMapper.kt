package com.example.repository.mapper

import com.example.entity.Category
import com.example.entity.Movie
import com.example.repository.dto.local.LocalMovieDto

class MovieLocalMapper {

    fun mapFromLocal(dto: LocalMovieDto, categories: List<Category> = emptyList()): Movie {
        return Movie(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            poster = dto.poster,
            productionYear = dto.productionYear,
            rating = dto.rating,
            categories = categories
        )
    }

    fun mapToLocal(domain: Movie): LocalMovieDto {
        return LocalMovieDto(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            poster = domain.poster,
            productionYear = domain.productionYear,
            rating = domain.rating
        )
    }

    fun mapListFromLocal(dtos: List<LocalMovieDto>, categoriesMap: Map<Long, List<Category>>): List<Movie> {
        return dtos.map { dto ->
            val categories = categoriesMap[dto.id] ?: emptyList()
            mapFromLocal(dto, categories)
        }
    }

    fun mapListToLocal(domains: List<Movie>): List<LocalMovieDto> {
        return domains.map { mapToLocal(it) }
    }
}
