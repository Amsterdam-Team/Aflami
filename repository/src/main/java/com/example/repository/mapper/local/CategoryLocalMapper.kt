package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.mapper.shared.mapToMovieCategory
import com.example.repository.mapper.shared.mapToTvShowCategory

class CategoryLocalMapper {

    fun mapToMovieCategories(localMovieCategories: List<LocalMovieCategoryDto>): List<MovieGenre> {
        return localMovieCategories.map { mapToMovieCategory(it) }
    }

    fun mapToTvShowCategories(localTvShowCategories: List<LocalTvShowCategoryDto>): List<TvShowGenre> {
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

    fun mapToCategories(localTvShowCategories: List<LocalTvShowCategoryDto>): List<Category> {
        return mapToTvShowCategories(localTvShowCategories).map { mapToCategory(it) }
    }

    private fun mapToCategory(tvShowGenre: TvShowGenre): Category {
        return Category(
            id = tvShowGenre.ordinal.toLong(),
            name = tvShowGenre.name,
            imageUrl = ""
        )
    }

    private fun mapToMovieCategory(localMovieCategory: LocalMovieCategoryDto): MovieGenre {
        return localMovieCategory.categoryId.mapToMovieCategory()
    }


    fun mapLocalCategoryToDomain(localCategory : LocalMovieCategoryDto): Category {
        return Category(
            id = localCategory.categoryId,
            name = localCategory.name,
            imageUrl = ""
        )
    }

    private fun mapToTvShowCategory(localTvShowCategory: LocalTvShowCategoryDto): TvShowGenre {
        return localTvShowCategory.categoryId.mapToTvShowCategory()
    }
}