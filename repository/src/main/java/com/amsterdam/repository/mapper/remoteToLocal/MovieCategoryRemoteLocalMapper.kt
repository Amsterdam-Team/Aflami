package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryDto

fun RemoteCategoryDto.toLocalMovieCategoryDto(storedLanguage: String): LocalMovieCategoryDto =
    LocalMovieCategoryDto(
        categoryId = id.toLong(),
        storedLanguage = storedLanguage,
        name = name
    )

fun List<RemoteCategoryDto>.toLocalDtoList(storedLanguage: String): List<LocalMovieCategoryDto> =
    map { it.toLocalMovieCategoryDto(storedLanguage) }