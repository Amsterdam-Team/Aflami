package com.example.repository.mapper.remote

import com.example.domain.useCase.genreTypes.TvShowGenre
import com.example.entity.TvShow
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.dto.remote.RemoteTvShowResponse

class RemoteTvShowMapper {

    fun mapToTvShow(remoteTvShowItemDto: RemoteTvShowItemDto): TvShow {
        return TvShow(
            id = remoteTvShowItemDto.id,
            name = remoteTvShowItemDto.title,
            description = remoteTvShowItemDto.overview,
            poster = remoteTvShowItemDto.posterPath.orEmpty(),
            productionYear = parseYear(remoteTvShowItemDto.releaseDate),
            categories = emptyList(),
            rating = remoteTvShowItemDto.voteAverage.toFloat(),
            popularity = remoteTvShowItemDto.popularity
        )
    }

    fun mapToTvShows(remoteTvShowResponse: RemoteTvShowResponse): List<TvShow> {
        return remoteTvShowResponse.results.map { mapToTvShow(it) }
    }

    private fun parseYear(date: String): Int {
        return date.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
    }
}