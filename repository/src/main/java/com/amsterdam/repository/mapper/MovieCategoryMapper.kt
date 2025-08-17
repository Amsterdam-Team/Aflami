package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.remote.CategoryRemoteDto

fun CategoryRemoteDto.toLocalMovieCategoryDto(): MovieCategoryLocalDto {
    return MovieCategoryLocalDto(
        categoryId = id.toLong()
    )
}

fun List<CategoryRemoteDto>.toLocalTvShowDtoList(): List<MovieCategoryLocalDto> {
    return map { it.toLocalMovieCategoryDto() }
}