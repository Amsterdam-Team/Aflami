package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre


class GetTopRatedTvShowsUseCase (private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(
        page: Int = 1,
    ): List<TvShow> {
        return tvShowRepository.getTopRatedTvShows(
            page = page,
        )
    }
}