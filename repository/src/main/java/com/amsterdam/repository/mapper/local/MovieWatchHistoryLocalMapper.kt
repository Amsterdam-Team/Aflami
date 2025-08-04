package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto

fun MovieWatchHistoryDto.toWatchHistoryEntity(localMovieDto: LocalMovieDto): MovieWatchHistory =
    MovieWatchHistory(
        movie = localMovieDto.toEntity(),
        lastWatchedTime = watchedDate
    )
