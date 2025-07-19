package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.mapper.shared.DtoMapper
import com.example.repository.mapper.shared.EntityMapper

class TvShowCategoryLocalMapper : EntityMapper<LocalTvShowCategoryDto, Category>,
    DtoMapper<Category, LocalTvShowCategoryDto> {
    override fun toEntity(dto: LocalTvShowCategoryDto): Category {
        return Category(
            id = dto.categoryId,
            name = dto.name,
            imageUrl = ""
        )
    }

    override fun toDto(entity: Category): LocalTvShowCategoryDto {
        return LocalTvShowCategoryDto(
            categoryId = entity.id,
            name = entity.name,
        )
    }
}