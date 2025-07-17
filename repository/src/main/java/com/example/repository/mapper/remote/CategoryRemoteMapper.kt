package com.example.repository.mapper.remote

import com.example.entity.Category
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.dto.remote.RemoteCategoryResponse

fun RemoteCategoryResponse.toCategories(): List<Category> {
    return genres.map { it.toCategory() }
}

fun RemoteCategoryResponse.toLocalMovieCategories(): List<LocalMovieCategoryDto> {
    return genres.map { it.toLocalMovieCategory() }
}

fun RemoteCategoryResponse.toLocalTvShowCategories(): List<LocalTvShowCategoryDto> {
    return genres.map { it.toLocalTvShowCategory() }
}

fun RemoteCategoryDto.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        image = ""
    )
}

fun RemoteCategoryDto.toLocalMovieCategory(): LocalMovieCategoryDto {
    return LocalMovieCategoryDto(
        categoryId = id,
        name = name
    )
}

fun RemoteCategoryDto.toLocalTvShowCategory(): LocalTvShowCategoryDto {
    return LocalTvShowCategoryDto(
        categoryId = id,
        name = name
    )
}
