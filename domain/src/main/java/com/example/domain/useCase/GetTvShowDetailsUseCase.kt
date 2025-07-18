package com.example.domain.useCase

import com.example.domain.repository.TvShowRepository
import com.example.entity.Actor
import com.example.entity.ProductionCompany
import com.example.entity.Review
import com.example.entity.Season
import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre

class GetTvShowDetailsUseCase(
    private val tvShowRepository: TvShowRepository,
) {

    suspend operator fun invoke(tvShowId: Long): TvShowDetails {
        val tvShow = tvShowRepository.getTvShowDetails(tvShowId)
        val actors = tvShowRepository.getTvShowCast(tvShowId)
        val seasons = tvShowRepository.getTvShowSeasons(tvShowId)
        val reviews = tvShowRepository.getTvShowReviews(tvShowId)
        val similarTvShows = tvShowRepository.getSimilarTvShows(tvShowId)
        val tvShowGallery = tvShowRepository.getTvShowGallery(tvShowId)
        val productionsCompanies = tvShowRepository.getProductionCompany(tvShowId)

        return TvShowDetails(
            tvShow = tvShow,
            categories = tvShow.categories,
            actors = actors,
            seasons = seasons,
            reviews = reviews,
            similarTvShows = similarTvShows,
            tvShowGallery = tvShowGallery,
            productionsCompanies = productionsCompanies
        )
    }

    data class TvShowDetails(
        val tvShow: TvShow,
        val categories: List<TvShowGenre>,
        val actors: List<Actor>,
        val seasons: List<Season>,
        val reviews: List<Review>,
        val similarTvShows: List<TvShow>,
        val tvShowGallery: List<String>,
        val productionsCompanies: List<ProductionCompany>
    )
}