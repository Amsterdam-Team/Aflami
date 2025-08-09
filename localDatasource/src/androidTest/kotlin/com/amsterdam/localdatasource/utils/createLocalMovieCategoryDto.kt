package com.amsterdam.localdatasource.utils

import com.amsterdam.repository.dto.local.LocalMovieCategoryDto

fun createLocalMovieCategoryDto(
    categoryId: Long = 0L,
    storedLanguage: String = "en",
    name: String = "Default Name"
): LocalMovieCategoryDto {
    return LocalMovieCategoryDto(
        categoryId = categoryId,
    )
}
