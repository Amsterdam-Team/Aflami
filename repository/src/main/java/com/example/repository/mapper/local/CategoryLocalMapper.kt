package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto

class CategoryLocalMapper {

    fun mapToMovieCategories(localMovieCategories: List<LocalMovieCategoryDto>): List<Category> {
        return localMovieCategories.map { mapToCategory(it) }
    }

    fun mapToTvShowCategories(localTvShowCategories: List<LocalTvShowCategoryDto>): List<Category> {
        return localTvShowCategories.map { mapToCategory(it) }
    }

    fun mapToLocalMovieCategories(categories: List<Category>): List<LocalMovieCategoryDto> {
        return categories.map { mapToLocalMovieCategory(it) }
    }


    private fun mapToCategory(localMovieCategory: LocalMovieCategoryDto): Category {
        return Category(
            id = localMovieCategory.categoryId,
            name = localMovieCategory.name,
            image = ""
        )
    }

    private fun mapToCategory(localTvShowCategory: LocalTvShowCategoryDto): Category {
        return Category(
            id = localTvShowCategory.categoryId,
            name = localTvShowCategory.name,
            image = ""
        )
    }

    private fun mapToLocalMovieCategory(category: Category): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = category.id,
            name = category.name,
        )
    }


    fun mapToLocalTvShowCategory(remoteTvShowCategoryDto: RemoteCategoryDto): LocalTvShowCategoryDto {
        return LocalTvShowCategoryDto(
            categoryId = remoteTvShowCategoryDto.id,
            name = remoteTvShowCategoryDto.name
        )
    }

    fun mapLocalCategoryToDomain(localCategory : LocalMovieCategoryDto): Category {
        return Category(
            id = localCategory.categoryId,
            name = localCategory.name,
            image = ""
        )
    }

    fun mapLocalCategoriesToDomain(localCategories: List<LocalMovieCategoryDto>)=
        localCategories.map(::mapLocalCategoryToDomain)

}