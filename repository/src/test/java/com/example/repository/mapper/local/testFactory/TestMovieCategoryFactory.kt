package com.example.repository.mapper.local.testFactory

import com.example.entity.Category
import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieCategoryDto

fun createLocalMovieCategoryDto(categoryId: Long = 1L, name: String = "Action"): LocalMovieCategoryDto {
    return LocalMovieCategoryDto(
        categoryId = categoryId,
        name = name
    )
}


fun createCategory(
    id: Long = 5,
    name: String = "Drama",
    imageUrl: String = "some_url"
): Category {
    return Category(
        id = id,
        name = name,
        imageUrl = imageUrl
    )
}

fun createLocalCategoryList(): List<LocalMovieCategoryDto> = listOf(
    LocalMovieCategoryDto(categoryId = 2, name = MovieGenre.SCIENCE_FICTION.name),
    LocalMovieCategoryDto(categoryId = 0, name = MovieGenre.ALL.name)
)