package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Category
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto

fun LocalTvShowCategoryDto.toEntity(): Category =
    Category(
        id = categoryId,
        name = name,
        imageUrl = ""
    )

fun List<LocalTvShowCategoryDto>.toEntityList(): List<Category> =
    map { it.toEntity() }
