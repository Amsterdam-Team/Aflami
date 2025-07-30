package com.amsterdam.repository.mapper.remote

import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase.TvShowDetails
import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.remote.TvShowDetailsRemoteResponse
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.mapper.shared.mapCategoryIdToTvShowGenre
import com.amsterdam.repository.utils.toSafeLocalDate
import javax.inject.Inject

class TvShowDetailsRemoteMapper @Inject constructor(
    private val productionCompanyMapper: ProductionCompanyRemoteMapper,
    private val castRemoteMapper: CastRemoteMapper,
    private val reviewRemoteMapper: ReviewRemoteMapper,
    private val galleryRemoteMapper: GalleryRemoteMapper,
    private val tvRemoteMapper: TvShowRemoteMapper,
    private val seasonRemoteMapper: SeasonRemoteMapper,
    private val posterRemoteMapper: PostersRemoteMapper,
) : EntityMapper<TvShowDetailsRemoteResponse, TvShowDetails> {
    override fun toEntity(dto: TvShowDetailsRemoteResponse): TvShowDetails {
        val tvShow = TvShow(
            id = dto.id,
            name = dto.title,
            description = dto.overview,
            posterUrl = dto.fullPosterPath.orEmpty(),
            airDate = dto.releaseDate.toSafeLocalDate(),
            categories = dto.genres.map { mapCategoryIdToTvShowGenre(it.id.toLong()) },
            rating = dto.voteAverage.toFloat(),
            popularity = dto.popularity,
            seasonCount = dto.seasonCount,
            originCountry = dto.originCountry.firstOrNull() ?: "",
            productionCompanies = productionCompanyMapper.toEntityList(dto.productionCompanies)
        )

        return with(dto) {
            TvShowDetails(
                tvShow = tvShow,
                actors = castRemoteMapper.toEntityList(credits.cast),
                seasons = seasonRemoteMapper.toEntityList(seasons),
                reviews = reviewRemoteMapper.toEntityList(reviews.results),
                similarTvShows = tvRemoteMapper.toEntityList(similar.results),
                gallery = galleryRemoteMapper.toEntity(images),
                posters = posterRemoteMapper.toEntity(images),
                productionsCompanies = productionCompanyMapper.toEntityList(productionCompanies),
            )
        }
    }
}