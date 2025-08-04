package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.remote.RemoteMovieItemDto
import com.amsterdam.repository.mapper.shared.RemoteToLocalMapper
import com.amsterdam.repository.utils.toSafeLocalDate
import javax.inject.Inject

class MovieRemoteLocalMapper @Inject constructor(): RemoteToLocalMapper<RemoteMovieItemDto, LocalMovieDto> {
    override fun toLocal(remote: RemoteMovieItemDto, args: List<Any>): LocalMovieDto {
        return LocalMovieDto(
            movieId = remote.id,
            storedLanguage = args.first().toString(),
            name = remote.title,
            description = remote.overview,
            poster = remote.fullPosterUrl.orEmpty(),
            releaseDate = remote.releaseDate.toSafeLocalDate(),
            rating = remote.voteAverage.toFloat(),
            popularity = remote.popularity,
            movieLength = remote.runtime,
            originCountry = remote.originCountry.firstOrNull() ?: ""
        )
    }
}