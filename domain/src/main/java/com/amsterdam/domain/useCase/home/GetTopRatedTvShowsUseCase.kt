package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.TvShow


class GetTopRatedTvShowsUseCase(private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(
        page: Int = 1,
        isCached: Boolean = true
    ): List<TvShow> {
        return if (isCached) tvShowRepository.getCachedTopRatedTvShows(page = page)
        else tvShowRepository.getRemoteTopRatedTvShows(page)
    }
}