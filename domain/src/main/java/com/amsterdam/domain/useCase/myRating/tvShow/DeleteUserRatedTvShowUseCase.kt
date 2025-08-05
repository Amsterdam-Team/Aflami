package com.amsterdam.domain.useCase.myRating.tvShow

import com.amsterdam.domain.repository.TvShowRepository

class DeleteUserRatedTvShowUseCase(
    private val tvShowRepository: TvShowRepository,
) {
    suspend fun deleteTvShowRate(tvShowId: Long) {
        tvShowRepository.deleteTvShowRate(tvShowId)
    }
}