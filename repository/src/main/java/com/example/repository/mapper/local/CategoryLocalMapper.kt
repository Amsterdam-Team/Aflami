package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.dto.remote.RemoteCategoryResponse

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

    fun mapToMovieCategories(remoteMovieCategoryResponse: RemoteCategoryResponse): List<Category> {
        return remoteMovieCategoryResponse.genres.map { mapToCategory(it) }
    }

    fun mapToTvShowCategories(remoteTvShowCategoryResponse: RemoteCategoryResponse): List<Category> {
        return remoteTvShowCategoryResponse.genres.map { mapToCategory(it) }
    }

    fun mapToLocalMovieCategories(remoteMovieCategoryResponse: RemoteCategoryResponse): List<LocalMovieCategoryDto> {
        return remoteMovieCategoryResponse.genres.map { mapToLocalMovieCategory(it) }
    }

    fun mapToLocalTvShowCategories(remoteTvShowCategoryResponse: RemoteCategoryResponse): List<LocalTvShowCategoryDto> {
        return remoteTvShowCategoryResponse.genres.map { mapToLocalTvShowCategory(it) }
    }

    private fun mapToCategory(localMovieCategory: LocalMovieCategoryDto): Category {
        return Category(
            id = localMovieCategory.categoryId,
            name = localMovieCategory.name,
            image = ""
        )
    }

    private fun mapToCategory(remoteMovieCategory: RemoteCategoryDto): Category {
        return Category(
            id = remoteMovieCategory.id,
            name = remoteMovieCategory.name,
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

    private fun mapToLocalMovieCategory(remoteMovieCategory: RemoteCategoryDto): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = remoteMovieCategory.id,
            name = remoteMovieCategory.name
        )
    }

    private fun mapToLocalTvShowCategory(remoteTvShowCategory: RemoteCategoryDto): LocalTvShowCategoryDto {
        return LocalTvShowCategoryDto(
            categoryId = remoteTvShowCategory.id,
            name = remoteTvShowCategory.name
        )
    }
}