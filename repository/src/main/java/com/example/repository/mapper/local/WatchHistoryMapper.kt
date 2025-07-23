package com.example.repository.mapper.local

import com.example.entity.WatchHistory
import com.example.repository.dto.local.WatchHistoryDto
import com.example.repository.mapper.shared.DtoMapper
import com.example.repository.mapper.shared.EntityMapper
import com.example.repository.utils.getDeviceLanguage

class WatchHistoryMapper : EntityMapper<WatchHistoryDto, WatchHistory>,
    DtoMapper<WatchHistory, WatchHistoryDto> {

    override fun toEntity(dto: WatchHistoryDto): WatchHistory {
        return WatchHistory(
            movieId = dto.movieId,
            lastWatchedTime = dto.lastWatchedTime
        )
    }

    override fun toDto(entity: WatchHistory, args: List<Any>): WatchHistoryDto {
        return WatchHistoryDto(
            movieId = entity.movieId,
            storedLanguage = getDeviceLanguage(),
            lastWatchedTime = entity.lastWatchedTime
        )
    }
}
