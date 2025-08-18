package com.amsterdam.domain.useCase.topRated

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.TvShow


class GetTopRatedTvShowsUseCase (private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(
        page: Int = 1,
    ): List<TvShow> {
        return tvShowRepository.getTopRatedTvShows(
            page = page,
        )
    }
}