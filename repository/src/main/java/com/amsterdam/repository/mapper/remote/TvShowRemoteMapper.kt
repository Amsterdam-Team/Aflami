package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.dto.remote.RemoteTvShowItemDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.mapper.shared.mapCategoryIdToTvShowGenre
import com.amsterdam.repository.utils.toSafeLocalDate
import javax.inject.Inject

class TvShowRemoteMapper @Inject constructor(
    private val productionCompanyMapper: ProductionCompanyRemoteMapper
) : EntityMapper<RemoteTvShowItemDto, TvShow> {
    override fun toEntity(dto: RemoteTvShowItemDto): TvShow {
       return toEntity(dto, true)
    }

    fun toEntity(dto: RemoteTvShowItemDto,isPoster : Boolean): TvShow {
        val imageUrl = if (isPoster) dto.fullPosterPath else dto.fullBackdropPath
        return TvShow(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            posterUrl = imageUrl.orEmpty(),
            airDate = dto.releaseDate.toSafeLocalDate(),
            categories = mapGenreIdsToCategories(dto.genreIds),
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity,
            seasonCount = dto.seasonCount,
            originCountry = dto.originCountry.firstOrNull() ?: "",
            productionCompanies = productionCompanyMapper.toEntityList(dto.productionCompanies)
        )
    }

    fun toEntityList(dtoList: List<RemoteTvShowItemDto>,isPoster: Boolean): List<TvShow> {
        return dtoList.map { toEntity(it, isPoster) }
    }

    private fun mapGenreIdsToCategories(genreIds: List<Int>): List<TvShowGenre> {
        return genreIds.map { mapCategoryIdToTvShowGenre(it.toLong()) }
    }
}