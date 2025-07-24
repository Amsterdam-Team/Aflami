package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.entity.TvShow

class GetPopularTvShowsUseCase (private val tvShowRepository: TvShowRepository) {
    suspend operator fun invoke(): List<TvShow> {
        return tvShowRepository.getPopularTvShows()
    }
}