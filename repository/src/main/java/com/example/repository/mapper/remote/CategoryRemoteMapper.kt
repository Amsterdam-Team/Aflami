package com.example.repository.mapper.remote

import com.example.entity.Category
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.dto.remote.RemoteCategoryResponse

class CategoryRemoteMapper {

    fun mapToCategories(remoteCategoryResponse: RemoteCategoryResponse): List<Category> {
        return remoteCategoryResponse.genres.map { mapToCategory(it) }
    }

    fun mapToLocalMovieCategories(remoteCategoryResponse: RemoteCategoryResponse): List<LocalMovieCategoryDto> {
        return remoteCategoryResponse.genres.map { mapToLocalMovieCategory(it) }
    }

    fun mapToLocalTvShowCategories(remoteCategoryResponse: RemoteCategoryResponse): List<LocalTvShowCategoryDto> {
        return remoteCategoryResponse.genres.map { mapToLocalTvShowCategory(it) }
    }

    private fun mapToCategory(remoteCategory: RemoteCategoryDto): Category {
        return Category(
            id = remoteCategory.id,
            name = remoteCategory.name,
            imageUrl = ""
        )
    }

    private fun mapToLocalMovieCategory(remoteCategory: RemoteCategoryDto): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = remoteCategory.id,
            name = remoteCategory.name
        )
    }

    private fun mapToLocalTvShowCategory(remoteCategory: RemoteCategoryDto): LocalTvShowCategoryDto {
        return LocalTvShowCategoryDto(
            categoryId = remoteCategory.id,
            name = remoteCategory.name
        )
    }
}