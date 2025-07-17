package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.repository.dto.local.LocalTvShowCategoryDto

fun LocalTvShowCategoryDto.toDomain(): Category = Category(
    id = categoryId,
    name = name,
    image = ""
)
