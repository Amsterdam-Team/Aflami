package com.amsterdam.repository.mapper

import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto

fun TvShowWatchHistoryDto.toEntity(dto: TvShowLocalDto): TvShowWatchHistory =
    TvShowWatchHistory(
        tvShow = dto.toEntity(),
        lastWatchedTime = watchedDate
    )