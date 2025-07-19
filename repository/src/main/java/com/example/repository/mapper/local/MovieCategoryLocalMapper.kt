package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.mapper.shared.DtoMapper
import com.example.repository.mapper.shared.EntityMapper

class MovieCategoryLocalMapper : EntityMapper<LocalMovieCategoryDto, Category>,
    DtoMapper<Category, LocalMovieCategoryDto> {

    override fun toEntity(dto: LocalMovieCategoryDto): Category {
        return Category(
            id = dto.categoryId,
            name = dto.name,
            imageUrl = ""
        )
    }

    override fun toDto(entity: Category): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = entity.id,
            name = entity.name,
        )
    }
}