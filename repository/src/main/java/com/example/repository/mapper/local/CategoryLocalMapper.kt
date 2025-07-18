package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.mapper.shared.toMovieCategory
import com.example.repository.mapper.shared.toTvShowCategory

class CategoryLocalMapper {

    fun toMovieCategories(localMovieCategories: List<LocalMovieCategoryDto>): List<MovieGenre> {
        return localMovieCategories.map { mapToMovieCategory(it) }
    }

    fun toTvShowCategories(localTvShowCategories: List<LocalTvShowCategoryDto>): List<TvShowGenre> {
        return localTvShowCategories.map { mapToTvShowCategory(it) }
    }

    fun mapToLocalMovieCategories(categories: List<Category>): List<LocalMovieCategoryDto> {
        return categories.map { mapToLocalMovieCategory(it) }
    }

    fun mapToLocalMovieCategory(category: Category): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = category.id,
            name = category.name,
        )
    }

    private fun mapToMovieCategory(localMovieCategory: LocalMovieCategoryDto): MovieGenre {
        return localMovieCategory.categoryId.toMovieCategory()
    }


    fun mapLocalCategoryToDomain(localCategory : LocalMovieCategoryDto): Category {
        return Category(
            id = localCategory.categoryId,
            name = localCategory.name,
            imageUrl = ""
        )
    }

    private fun mapToTvShowCategory(localTvShowCategory: LocalTvShowCategoryDto): TvShowGenre {
        return localTvShowCategory.categoryId.toTvShowCategory()
    }
}