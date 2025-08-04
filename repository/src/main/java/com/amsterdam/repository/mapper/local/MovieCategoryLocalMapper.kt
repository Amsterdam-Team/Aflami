package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Category
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto

fun LocalMovieCategoryDto.toEntityCategory(): Category =
    Category(
        id = categoryId,
        name = name,
        imageUrl = ""
    )

fun List<LocalMovieCategoryDto>.toEntityCategoryList(): List<Category> =
    map { it.toEntityCategory() }


