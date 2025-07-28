package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.amsterdam.repository.mapper.shared.DtoMapper
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.utils.getDeviceLanguage
import javax.inject.Inject

class TvShowWatchHistoryMapper @Inject constructor() : EntityMapper<TvShowWatchHistoryDto, TvShowWatchHistory>,
    DtoMapper<TvShowWatchHistory, TvShowWatchHistoryDto> {

    override fun toEntity(dto: TvShowWatchHistoryDto): TvShowWatchHistory {
        return TvShowWatchHistory(
            tvShowId = dto.tvShowId,
            lastWatchedTime = dto.lastWatchedTime
        )
    }

    override fun toDto(entity: TvShowWatchHistory, args: List<Any>): TvShowWatchHistoryDto {
        return TvShowWatchHistoryDto(
            tvShowId = entity.tvShowId,
            storedLanguage = getDeviceLanguage(),
            lastWatchedTime = entity.lastWatchedTime
        )
    }
}