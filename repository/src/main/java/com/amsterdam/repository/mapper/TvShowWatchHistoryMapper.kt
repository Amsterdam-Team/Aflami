package com.amsterdam.repository.mapper

import com.amsterdam.domain.utils.TvShowWatchHistory
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto

fun TvShowWatchHistoryDto.toEntity(dto: TvShowLocalDto): TvShowWatchHistory {
    return TvShowWatchHistory(
        tvShow = dto.toEntity(),
        lastWatchedTime = watchedDate
    )
}