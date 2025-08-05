package com.amsterdam.localdatasource.utils

import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto

fun createLocalTvShowCategoryDto(
    categoryId: Long = 0,
    storedLanguage: String = "en",
    name: String = "Default"
): LocalTvShowCategoryDto {
    return LocalTvShowCategoryDto(categoryId, storedLanguage, name)
}