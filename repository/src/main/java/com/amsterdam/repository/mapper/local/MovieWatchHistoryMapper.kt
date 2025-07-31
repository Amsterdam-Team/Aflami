package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.mapper.shared.DtoMapper
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class MovieWatchHistoryMapper @Inject constructor() :
    EntityMapper<MovieWatchHistoryDto, MovieWatchHistory>,
    DtoMapper<MovieWatchHistory, MovieWatchHistoryDto> {

    override fun toEntity(dto: MovieWatchHistoryDto): MovieWatchHistory {
        return MovieWatchHistory(
            movieId = dto.movieId,
            lastWatchedTime = dto.lastWatchedTime
        )
    }

    override fun toDto(entity: MovieWatchHistory, args: List<Any>): MovieWatchHistoryDto {
        return MovieWatchHistoryDto(
            movieId = entity.movieId,
            storedLanguage = args.first().toString(),
            lastWatchedTime = entity.lastWatchedTime
        )
    }
}