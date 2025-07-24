package com.example.repository.mapper.remoteToLocal

import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.mapper.shared.RemoteToLocalMapper
import kotlinx.datetime.toLocalDate

class MovieRemoteLocalMapper() : RemoteToLocalMapper<RemoteMovieItemDto, LocalMovieDto> {
    override fun toLocal(remote: RemoteMovieItemDto, args: List<Any>): LocalMovieDto {
        return LocalMovieDto(
            movieId = remote.id,
            storedLanguage = args.first().toString(),
            name = remote.title,
            description = remote.overview,
            poster = remote.fullPosterUrl.orEmpty(),
            releaseDate = remote.releaseDate.toLocalDate(),
            rating = remote.voteAverage.toFloat(),
            popularity = remote.popularity,
            movieLength = remote.runtime,
            originCountry = remote.originCountry.firstOrNull() ?: "",
            hasVideo = remote.video
        )
    }
}