package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Category
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.mapper.shared.DtoMapper
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class MovieCategoryLocalMapper @Inject constructor(): EntityMapper<LocalMovieCategoryDto, Category>,
    DtoMapper<Category, LocalMovieCategoryDto> {

    override fun toEntity(dto: LocalMovieCategoryDto): Category {
        return Category(
            id = dto.categoryId,
            name = dto.name,
            imageUrl = ""
        )
    }

    override fun toDto(entity: Category, args: List<Any>): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = entity.id,
            storedLanguage = args.first().toString(),
            name = entity.name,
        )
    }
}