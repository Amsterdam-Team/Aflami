package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.repository.dto.local.LocalMovieCategoryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


fun Category.toLocal(): LocalMovieCategoryDto = LocalMovieCategoryDto(
    categoryId = id,
    name = name,
)

fun LocalMovieCategoryDto.toDomain(): Category = Category(
    id = categoryId,
    name = name,
    image = ""
)

fun Flow<List<LocalMovieCategoryDto>>.toMovieCategoryListFlow(): Flow<List<Category>> {
    return this.map { list -> list.map { it.toDomain() } }
}

