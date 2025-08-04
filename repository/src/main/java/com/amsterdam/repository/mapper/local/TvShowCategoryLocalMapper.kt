package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Category
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto

fun LocalTvShowCategoryDto.toCategoryEntity(): Category =
    Category(
        id = categoryId,
        name = name,
        imageUrl = ""
    )

fun List<LocalTvShowCategoryDto>.toCategoryEntityList(): List<Category> =
    map { it.toCategoryEntity() }
