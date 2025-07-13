package com.example.repository.mapper.remote

import com.example.entity.GenreType
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

    fun mapToTvGenreId(genreType: GenreType): Int? {
        return when (genreType) {
            GenreType.ANIMATION -> 16
            GenreType.COMEDY -> 35
            GenreType.CRIME -> 80
            GenreType.DOCUMENTARY -> 99
            GenreType.DRAMA -> 18
            GenreType.FAMILY -> 10751
            GenreType.MYSTERY -> 9648
            GenreType.SCIENCE_FICTION -> 10765
            GenreType.WESTERN -> 37
            GenreType.ROMANCE -> 10749
            GenreType.MUSIC -> 10402
            GenreType.FANTASY -> 10765
            else -> null
        }
    }
}