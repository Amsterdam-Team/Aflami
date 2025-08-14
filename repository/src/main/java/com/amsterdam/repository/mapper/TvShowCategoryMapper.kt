package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.remote.RemoteCategoryDto

fun RemoteCategoryDto.toLocalTvShowCategoryDto(storedLanguage: String): TvShowCategoryLocalDto =
    TvShowCategoryLocalDto(
        categoryId = this.id.toLong()
    )

fun List<RemoteCategoryDto>.toLocalTvShowDtoList(storedLanguage: String): List<TvShowCategoryLocalDto> =
    map { it.toLocalTvShowCategoryDto(storedLanguage) }