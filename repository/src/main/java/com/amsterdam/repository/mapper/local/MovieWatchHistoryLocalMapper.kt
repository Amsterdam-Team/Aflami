package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.MovieWatchHistory
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class MovieWatchHistoryLocalMapper @Inject constructor(private val movieLocalMapper: MovieLocalMapper) :  EntityMapper<LocalMovieDto, MovieWatchHistory> {
    override fun toEntity(dto: LocalMovieDto): MovieWatchHistory {
        return MovieWatchHistory(
            movie = movieLocalMapper.toEntity(dto),
            lastWatchedTime = dto.insertedDate
        )
    }

}