package com.example.repository.mapper.remote

import com.example.domain.mapper.DomainMapper
import com.example.entity.Category
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.dto.remote.RemoteCategoryResponse

class CategoryRemoteMapper: DomainMapper<List<Category>, RemoteCategoryResponse> {

    fun toLocalMovieCategories(remoteCategoryResponse: RemoteCategoryResponse): List<LocalMovieCategoryDto> {
        return remoteCategoryResponse.genres.map { toLocalMovieCategory(it) }
    }

    fun toLocalTvShowCategories(remoteCategoryResponse: RemoteCategoryResponse): List<LocalTvShowCategoryDto> {
        return remoteCategoryResponse.genres.map { toLocalTvShowCategory(it) }
    }

    private fun toLocalMovieCategory(remoteCategory: RemoteCategoryDto): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = remoteCategory.id,
            name = remoteCategory.name
        )
    }

    private fun toLocalTvShowCategory(remoteCategory: RemoteCategoryDto): LocalTvShowCategoryDto {
        return LocalTvShowCategoryDto(
            categoryId = remoteCategory.id,
            name = remoteCategory.name
        )
    }

    fun mapToCategories(remoteCategoryResponse: RemoteCategoryResponse): List<Category> {
        return remoteCategoryResponse.genres.map { mapToCategory(it) }
    }
    fun mapToLocalMovieCategories(remoteCategoryResponse: RemoteCategoryResponse): List<LocalMovieCategoryDto> {
        return remoteCategoryResponse.genres.map { mapToLocalMovieCategory(it) }
    }

    override fun toDomain(dto: RemoteCategoryResponse): List<Category> {
        return dto.genres.map { mapToCategory(it) }
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