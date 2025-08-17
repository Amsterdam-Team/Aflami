package com.amsterdam.repository.mapper

import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto

fun MovieWatchHistoryDto.toEntity(movieLocalDto: MovieLocalDto): MovieWatchHistory {
    return MovieWatchHistory(
        movie = movieLocalDto.toEntity(),
        lastWatchedTime = watchedDate
    )
}