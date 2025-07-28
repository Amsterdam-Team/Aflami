package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.shared.RemoteToLocalMapper
import com.amsterdam.repository.utils.toSafeLocalDate
import javax.inject.Inject

class TvShowRemoteDetailsLocalMapper @Inject constructor() :
    RemoteToLocalMapper<TvShowDetailsRemoteResponse, LocalTvShowDto> {
    override fun toLocal(remote: TvShowDetailsRemoteResponse, args: List<Any>): LocalTvShowDto {
        return LocalTvShowDto(
            tvShowId = remote.id,
            storedLanguage = args.first().toString(),
            name = remote.title,
            description = remote.overview,
            poster = remote.fullPosterPath.orEmpty(),
            airDate = remote.releaseDate.toSafeLocalDate(),
            rating = remote.voteAverage.toFloat(),
            popularity = remote.popularity,
            seasonCount = remote.seasonCount,
            originCountry = remote.originCountry.firstOrNull() ?: "",
        )
    }
}