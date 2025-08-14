package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.remote.CategoryRemoteDto

fun CategoryRemoteDto.toLocalTvShowCategoryDto(storedLanguage: String): TvShowCategoryLocalDto =
    TvShowCategoryLocalDto(
        categoryId = this.id.toLong()
    )

fun List<CategoryRemoteDto>.toLocalTvShowDtoList(storedLanguage: String): List<TvShowCategoryLocalDto> =
    map { it.toLocalTvShowCategoryDto(storedLanguage) }