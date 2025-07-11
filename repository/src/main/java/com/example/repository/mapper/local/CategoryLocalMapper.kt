package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.repository.dto.local.LocalCategoryDto

class CategoryLocalMapper {

    fun mapFromLocal(dto: LocalCategoryDto): Category {
        return Category(
            id = dto.id,
            name = dto.name,
            image = ""
        )
    }

    fun mapToLocal(domain: Category): LocalCategoryDto {
        return LocalCategoryDto(
            id = domain.id,
            name = domain.name,
        )
    }

    fun mapListFromLocal(dtos: List<LocalCategoryDto>): List<Category> {
        return dtos.map { mapFromLocal(it) }
    }

    fun mapListToLocal(domains: List<Category>): List<LocalCategoryDto> {
        return domains.map { mapToLocal(it) }
    }
}