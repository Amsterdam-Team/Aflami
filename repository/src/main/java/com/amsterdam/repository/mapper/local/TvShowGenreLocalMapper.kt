package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.mapper.shared.mapCategoryIdToTvShowGenre
import javax.inject.Inject

class TvShowGenreLocalMapper @Inject constructor(): EntityMapper<LocalTvShowCategoryDto, TvShowGenre> {
    override fun toEntity(dto: LocalTvShowCategoryDto): TvShowGenre {
        return mapCategoryIdToTvShowGenre(dto.categoryId)
    }
}