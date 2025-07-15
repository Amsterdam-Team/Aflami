package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.dto.remote.RemoteCategoryResponse

class CategoryLocalMapper {

    fun mapFromMovieLocal(dto: LocalMovieCategoryDto): Category {
        return Category(
            id = dto.categoryId,
            name = dto.name,
            image = ""
        )
    }

    fun mapFromTvShowLocal(dto: LocalTvShowCategoryDto): Category {
        return Category(
            id = dto.categoryId,
            name = dto.name,
            image = ""
        )
    }

    fun mapToLocal(domain: Category): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = domain.id,
            name = domain.name,
        )
    }

    fun mapListFromMovieLocal(dtos: List<LocalMovieCategoryDto>): List<Category> {
        return dtos.map { mapFromMovieLocal(it) }
    }

    fun mapListFromTvShowLocal(dtos: List<LocalTvShowCategoryDto>): List<Category> {
        return dtos.map { mapFromTvShowLocal(it) }
    }

    fun mapListToLocal(domains: List<Category>): List<LocalMovieCategoryDto> {
        return domains.map { mapToLocal(it) }
    }

    fun mapToLocalMovieCategories(remoteMovieCategoryResponse: RemoteCategoryResponse): List<LocalMovieCategoryDto> {
        return remoteMovieCategoryResponse.genres.map { mapToLocalMovieCategory(it) }
    }

    fun mapToLocalMovieCategory(remoteMovieCategoryDto: RemoteCategoryDto): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = remoteMovieCategoryDto.id,
            name = remoteMovieCategoryDto.name
        )
    }

    fun mapToLocalTvShowCategories(remoteTvShowCategoryResponse: RemoteCategoryResponse): List<LocalTvShowCategoryDto> {
        return remoteTvShowCategoryResponse.genres.map { mapToLocalTvShowCategory(it) }
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