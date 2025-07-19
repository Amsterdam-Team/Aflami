package com.example.repository.mapper.remoteToLocal

import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.mapper.shared.RemoteToLocalMapper
import com.example.repository.utils.DateParser

class MovieRemoteLocalMapper(
    private val dateParser: DateParser
) : RemoteToLocalMapper<RemoteMovieItemDto, LocalMovieDto> {
    override fun toLocal(remote: RemoteMovieItemDto): LocalMovieDto {
        return LocalMovieDto(
            movieId = remote.id,
            name = remote.title,
            description = remote.overview,
            poster = remote.fullPosterUrl.orEmpty(),
            productionYear = dateParser.parseYear(remote.releaseDate),
            rating = remote.voteAverage.toFloat(),
            popularity = remote.popularity,
            movieLength = remote.runtime,
            originCountry = remote.originCountry.firstOrNull() ?: "",
            hasVideo = remote.video
        )
    }
}