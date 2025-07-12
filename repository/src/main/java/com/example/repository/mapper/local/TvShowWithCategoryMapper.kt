package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.entity.TvShow
import com.example.repository.dto.local.LocalCategoryDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategories

class TvShowWithCategoryMapper {

    fun mapFromLocal(tvShowWithCategories: TvShowWithCategories): TvShow {
        return TvShow(
            id = tvShowWithCategories.tvShow.tvId,
            name = tvShowWithCategories.tvShow.name,
            description = tvShowWithCategories.tvShow.description,
            poster = tvShowWithCategories.tvShow.poster,
            productionYear = tvShowWithCategories.tvShow.productionYear,
            rating = tvShowWithCategories.tvShow.rating,
            categories = tvShowWithCategories.categories.map { mapCategoryFromLocal(it) }
        )
    }

    private fun mapCategoryFromLocal(localCategory: LocalCategoryDto): Category {
        return Category(
            id = localCategory.categoryId,
            name = localCategory.name,
            image = ""
        )
    }

    fun mapToLocal(tvShow: TvShow): LocalTvShowDto {
        return LocalTvShowDto(
            tvId = tvShow.id,
            name = tvShow.name,
            description = tvShow.description,
            poster = tvShow.poster,
            productionYear = tvShow.productionYear,
            rating = tvShow.rating
        )
    }
}