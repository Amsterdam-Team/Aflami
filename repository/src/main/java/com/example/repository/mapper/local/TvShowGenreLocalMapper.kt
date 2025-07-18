package com.example.repository.mapper.local

import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.mapper.shared.EntityMapper
import com.example.repository.mapper.shared.toTvShowCategory

class TvShowGenreLocalMapper : EntityMapper<LocalTvShowCategoryDto, TvShowGenre> {
    override fun toEntity(dto: LocalTvShowCategoryDto): TvShowGenre {
        return dto.categoryId.toTvShowCategory()
    }
}