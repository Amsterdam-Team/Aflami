package com.example.repository.mapper.local

import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.mapper.shared.EntityMapper
import com.example.repository.mapper.shared.toMovieCategory

class MovieGenreLocalMapper : EntityMapper<LocalMovieCategoryDto, MovieGenre> {
    override fun toEntity(dto: LocalMovieCategoryDto): MovieGenre {
        return dto.categoryId.toMovieCategory()
    }
}