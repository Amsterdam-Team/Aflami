package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Category
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.mapper.shared.DtoMapper
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class TvShowCategoryLocalMapper @Inject constructor(): EntityMapper<LocalTvShowCategoryDto, Category>,
    DtoMapper<Category, LocalTvShowCategoryDto> {
    override fun toEntity(dto: LocalTvShowCategoryDto): Category {
        return Category(
            id = dto.categoryId,
            name = dto.name,
            imageUrl = ""
        )
    }

    override fun toDto(entity: Category, args: List<Any>): LocalTvShowCategoryDto {
        return LocalTvShowCategoryDto(
            categoryId = entity.id,
            storedLanguage = args.first().toString(),
            name = entity.name,
        )
    }
}