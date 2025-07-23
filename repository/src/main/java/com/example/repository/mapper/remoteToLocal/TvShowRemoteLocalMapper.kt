package com.example.repository.mapper.remoteToLocal

import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.mapper.shared.RemoteToLocalMapper
import com.example.repository.utils.DateParser

class TvShowRemoteLocalMapper(
    private val dateParser: DateParser
) : RemoteToLocalMapper<RemoteTvShowItemDto, LocalTvShowDto> {
    override fun toLocal(remote: RemoteTvShowItemDto, args: List<Any>): LocalTvShowDto {
        return LocalTvShowDto(
            tvShowId = remote.id,
            storedLanguage = args.first().toString(),
            name = remote.title,
            description = remote.overview,
            poster = remote.fullPosterPath.orEmpty(),
            productionYear = dateParser.parseYear(remote.releaseDate),
            rating = remote.voteAverage.toFloat(),
            popularity = remote.popularity,
            seasonCount = remote.seasonCount,
            originCountry = remote.originCountry.firstOrNull() ?: "",
        )
    }
}