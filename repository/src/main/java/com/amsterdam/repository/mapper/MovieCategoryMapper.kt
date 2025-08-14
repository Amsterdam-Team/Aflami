package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.remote.RemoteCategoryDto

fun RemoteCategoryDto.toLocalMovieCategoryDto(): MovieCategoryLocalDto =
    MovieCategoryLocalDto(
        categoryId = id.toLong()
    )

fun List<RemoteCategoryDto>.toLocalTvShowDtoList(): List<MovieCategoryLocalDto> =
    map { it.toLocalMovieCategoryDto() }