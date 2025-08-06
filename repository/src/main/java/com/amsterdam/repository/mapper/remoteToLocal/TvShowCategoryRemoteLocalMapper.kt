package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryDto

fun RemoteCategoryDto.toLocalTvShowCategoryDto(storedLanguage: String): LocalTvShowCategoryDto =
    LocalTvShowCategoryDto(
        categoryId = this.id.toLong()
    )

fun List<RemoteCategoryDto>.toLocalTvShowCategoryDtoList(storedLanguage: String): List<LocalTvShowCategoryDto> =
    map { it.toLocalTvShowCategoryDto(storedLanguage) }