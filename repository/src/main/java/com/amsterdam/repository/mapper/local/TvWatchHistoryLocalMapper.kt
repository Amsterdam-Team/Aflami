package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class TvWatchHistoryLocalMapper @Inject constructor(private val tvShowLocalMapper: TvShowLocalMapper) :  EntityMapper<LocalTvShowDto,
        TvShowWatchHistory> {

    override fun toEntity(dto: LocalTvShowDto): TvShowWatchHistory {
        return TvShowWatchHistory(
            tvShow = tvShowLocalMapper.toEntity(dto),
            lastWatchedTime = dto.insertedDate
        )
    }

}