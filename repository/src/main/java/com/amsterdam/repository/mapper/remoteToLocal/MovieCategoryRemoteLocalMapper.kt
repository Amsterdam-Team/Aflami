package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryDto

fun RemoteCategoryDto.toLocalMovieCategoryDto(): LocalMovieCategoryDto =
    LocalMovieCategoryDto(
        categoryId = id.toLong()
    )

fun List<RemoteCategoryDto>.toLocalDtoList(): List<LocalMovieCategoryDto> =
    map { it.toLocalMovieCategoryDto() }