package com.amsterdam.domain.useCase.myRating.tvShow

import com.amsterdam.domain.repository.TvShowRepository

class SetUserTvShowRatingUseCase(
    private val tvShowRepository: TvShowRepository,
) {
    suspend fun setUserMovieRate(rate: Int, tvShowId: Long){
        tvShowRepository.setTvShowRate(rate = rate, tvShowId = tvShowId)
    }
}