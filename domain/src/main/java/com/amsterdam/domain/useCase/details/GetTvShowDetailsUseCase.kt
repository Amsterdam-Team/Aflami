package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.common.AddTvShowWatchHistoryUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.ProductionCompany
import com.amsterdam.entity.Review
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow

class GetTvShowDetailsUseCase(
    private val tvShowRepository: TvShowRepository,
    private val addTvShowWatchHistoryUseCase: AddTvShowWatchHistoryUseCase,
) {

    suspend operator fun invoke(tvShowId: Long): TvShowDetails {
        return tvShowRepository.getTvShowDetails(tvShowId)
            .also { addTvShowWatchHistoryUseCase(tvShowId) }
    }

    data class TvShowDetails(
        val tvShow: TvShow,
        val actors: List<Actor>,
        val seasons: List<Season>,
        val reviews: List<Review>,
        val similarTvShows: List<TvShow>,
        val gallery: List<String>,
        val posters: List<String>,
        val productionsCompanies: List<ProductionCompany>,
        val userRate: Int?
    )
}