package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.common.AddTvShowWatchHistoryUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.ProductionCompany
import com.amsterdam.entity.Review
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre

class GetTvShowDetailsUseCase (
    private val tvShowRepository: TvShowRepository,
    private val addTvShowWatchHistoryUseCase: AddTvShowWatchHistoryUseCase
) {

    suspend operator fun invoke(tvShowId: Long): TvShowDetails {
        val tvShow = tvShowRepository.getTvShowDetails(tvShowId)
        val actors = tvShowRepository.getTvShowCast(tvShowId)
        val seasons = tvShowRepository.getTvShowSeasons(tvShowId)
        val reviews = tvShowRepository.getTvShowReviews(tvShowId)
        val similarTvShows = tvShowRepository.getSimilarTvShows(tvShowId)
        val gallery = tvShowRepository.getTvShowGallery(tvShowId)
        val productionsCompanies = tvShowRepository.getProductionCompany(tvShowId)

        return TvShowDetails(
            tvShow = tvShow,
            categories = tvShow.categories,
            actors = actors,
            seasons = seasons,
            reviews = reviews,
            similarTvShows = similarTvShows,
            gallery = gallery,
            productionsCompanies = productionsCompanies
        ).also {
            addTvShowWatchHistoryUseCase(tvShowId)
        }
    }

    data class TvShowDetails(
        val tvShow: TvShow,
        val categories: List<TvShowGenre>,
        val actors: List<Actor>,
        val seasons: List<Season>,
        val reviews: List<Review>,
        val similarTvShows: List<TvShow>,
        val gallery: List<String>,
        val productionsCompanies: List<ProductionCompany>
    )
}