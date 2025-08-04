package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Category
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto

fun LocalMovieCategoryDto.toEntity(): Category =
    Category(
        id = categoryId,
        name = name,
        imageUrl = ""
    )

fun List<LocalMovieCategoryDto>.toEntityList(): List<Category> =
    map { it.toEntity() }


