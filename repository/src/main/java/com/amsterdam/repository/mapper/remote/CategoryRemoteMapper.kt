package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Category
import com.amsterdam.repository.dto.remote.RemoteCategoryDto

fun RemoteCategoryDto.toCategoryEntity(): Category =
    Category(
        id = this.id.toLong(),
        name = this.name,
        imageUrl = ""
    )

fun List<RemoteCategoryDto>.toCategoryEntityList(): List<Category> = map { it.toCategoryEntity() }