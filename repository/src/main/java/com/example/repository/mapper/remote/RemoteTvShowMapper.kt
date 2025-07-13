package com.example.repository.mapper.remote

import com.example.domain.useCase.genreTypes.TvShowGenre
import com.example.entity.TvShow
import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.dto.remote.RemoteTvShowResponse

class RemoteTvShowMapper {

    fun mapToDomain(dto: RemoteTvShowItemDto): TvShow {
        return TvShow(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            poster = dto.posterPath.orEmpty(),
            productionYear = parseYear(dto.releaseDate),
            categories = emptyList(),
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity
        )
    }

    fun mapResponseToDomain(response: RemoteTvShowResponse): List<TvShow> {
        return response.results.map { mapToDomain(it) }
    }

    private fun parseYear(date: String): Int {
        return date.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull() ?: 0
    }

    fun mapToShowTvGenreId(tvShowGenre: TvShowGenre): Int? {
        return when (tvShowGenre) {
            TvShowGenre.ALL -> null
            TvShowGenre.ACTION_ADVENTURE -> 10759
            TvShowGenre.ANIMATION -> 16
            TvShowGenre.COMEDY -> 35
            TvShowGenre.CRIME -> 80
            TvShowGenre.DOCUMENTARY -> 99
            TvShowGenre.DRAMA -> 18
            TvShowGenre.FAMILY -> 10751
            TvShowGenre.KIDS -> 10762
            TvShowGenre.MYSTERY -> 9648
            TvShowGenre.NEWS -> 10763
            TvShowGenre.REALITY -> 10764
            TvShowGenre.SCIENCE_FICTION -> 10765
            TvShowGenre.FANTASY -> 10765
            TvShowGenre.SOAP -> 10766
            TvShowGenre.TALK -> 10767
            TvShowGenre.WAR_POLITICS -> 10768
            TvShowGenre.WESTERN -> 37
            TvShowGenre.ROMANCE -> 10749
        }
    }

}