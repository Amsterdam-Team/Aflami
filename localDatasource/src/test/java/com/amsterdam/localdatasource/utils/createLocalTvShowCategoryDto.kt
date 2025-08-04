package com.amsterdam.localdatasource.utils

fun createLocalTvShowCategoryDto(
    categoryId: Long = 0,
    storedLanguage: String = "en",
    name: String = "Default"
): LocalTvShowCategoryDto {
    return LocalTvShowCategoryDto(categoryId, storedLanguage, name)
}