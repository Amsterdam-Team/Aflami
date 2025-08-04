package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.TvShowWatchHistory
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto

fun TvShowWatchHistoryDto.toWatchHistoryEntity(dto: LocalTvShowDto): TvShowWatchHistory =
    TvShowWatchHistory(
        tvShow = dto.toEntity(),
        lastWatchedTime = watchedDate
    )